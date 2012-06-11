package bibtex.parser.tags;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import bibtex.parser.Processor;

public class TagAnalizer {
	
	private static final int NUMBER_OF_PAPERS = 2688;
	private static final String FILE_PATH = Processor.BIB_HOME + "/Artigos_formatados.xlsx";
	private static final byte REF_ID_INDEX = 0;
	private static final byte SELECTED_INDEX = 7;
	private static final byte TAGS_INDEX = 8;
	private static final byte RESEARCH_TYPE_INDEX = 9;

	private Map<String, ArrayList<Integer>> tagMapRefs;
	private Map<String, ArrayList<Integer>> excludedTagsRefs;
	private List<DSLTypeAndDomain> dslTypes;
	private Workbook workbook;
	private int numberOfIncluded;
	private int numberOfExcluded;
	private int numberOfIncludedAndClassified;
	private int numberOfIncludedNotClassifiedYet;

	public TagAnalizer() throws Exception {
		this.tagMapRefs = new HashMap<String, ArrayList<Integer>>();
		this.excludedTagsRefs = new HashMap<String, ArrayList<Integer>>();
		this.dslTypes = new ArrayList<DSLTypeAndDomain>();
		this.workbook = new XSSFWorkbook(new FileInputStream(FILE_PATH));
	}

	/**
	 * This method counts the number occurrences of each research type. It DOES 
	 * NOT affect any class attributes (references lists and/or dsl types) 
	 */
	public void printResearchTypeTags() {
		Sheet papersSheet = this.workbook.getSheet("Papers");
		
		Map<String, Integer> rMap = new HashMap<String, Integer>();
		for (int i = 1; i <= NUMBER_OF_PAPERS; i++) {
			Row row = papersSheet.getRow(i);
			String selected = row.getCell(SELECTED_INDEX).getStringCellValue();
			if (selected != null && selected.equalsIgnoreCase("included")) {
				// Only considering "included" papers
				String tagsCellContent = row.getCell(RESEARCH_TYPE_INDEX).getStringCellValue();
				if (tagsCellContent != null && !tagsCellContent.trim().equals("")) {
					String[] splittedTags = tagsCellContent.split(",");
					for (int j = 0; j < splittedTags.length; j++) {
						String key = splittedTags[j].trim().toLowerCase();
						if (rMap.containsKey(key)) {
							int number = rMap.get(key);
							number++;
							rMap.put(key, number);
						} else {
							rMap.put(key, 0);
						}
					}
				} 
			}
		}
		Set<String> keySet = rMap.keySet();
		for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
			String currentKey = iterator.next();
			System.out.printf("%25s %5d \n", currentKey, rMap.get(currentKey));	
		}
	}
	
	/**
	 * Process all tags individually without associating DSL research type 
	 * (internal/external dsl, method/process, technique, tools, DSML, ADL and 
	 * DSAL).
	 * 
	 * @param printResult true if the result should be printed
	 * 					  false otherwise
	 */
	public void processAllTags(boolean printResult) {
		Sheet papersSheet = this.workbook.getSheet("Papers");
		for (int i = 1; i <= NUMBER_OF_PAPERS; i++) {
			Row row = papersSheet.getRow(i);
			String selected = row.getCell(SELECTED_INDEX).getStringCellValue();
			if (selected != null) {
				// Once the paper has been classified, check if it
				// is included or excluded
				if (selected.equalsIgnoreCase("included")) {
					this.numberOfIncluded++;
					String tagsCellContent = row.getCell(TAGS_INDEX).getStringCellValue();
					if (tagsCellContent != null && !tagsCellContent.trim().equals("")) {
						this.numberOfIncludedAndClassified++;
						this.processCurrentTag(row, tagsCellContent, this.tagMapRefs);
					} else {
						this.numberOfIncludedNotClassifiedYet++;
					}
				} else if (selected.equalsIgnoreCase("excluded")){
					this.numberOfExcluded++;
				}
			}
		}
		if (printResult) {
			this.printTagsStatus(this.tagMapRefs);
		}
	}

	/**
	 * Process all tags associating with their respective DSL research type 
	 * (internal/external dsl, method/process, technique, tools, DSML, ADL and 
	 * DSAL).
	 * 
	 * @param printResult true if the result should be printed
	 * 					  false otherwise
	 */
	public void processTagsAndDomainsStatus(boolean printResult) {
		Sheet papersSheet = this.workbook.getSheet("Papers");
		for (int i = 1; i <= NUMBER_OF_PAPERS; i++) {
			Row row = papersSheet.getRow(i);
			String selected = row.getCell(SELECTED_INDEX).getStringCellValue();
			if (selected != null) {
				// Once the paper has been classified, check if it
				// is included or excluded
				if (selected.equalsIgnoreCase("included")) {
					this.numberOfIncluded++;
					String tagsCellContent = row.getCell(TAGS_INDEX).getStringCellValue();
					String researchCellContent = row.getCell(RESEARCH_TYPE_INDEX).getStringCellValue();
					int refID = (int) row.getCell(REF_ID_INDEX).getNumericCellValue();
					if (tagsCellContent != null && !tagsCellContent.trim().equals("")) {
						this.numberOfIncludedAndClassified++;
						List<String> splittedTags = Arrays.asList(tagsCellContent.split(","));
						List<String> subDomains = new ArrayList<String>();
						DSLTypeAndDomain currentDsl = null;
						for (int tagIndex = 0; tagIndex < splittedTags.size(); tagIndex++) {
							String currentTag = splittedTags.get(tagIndex);
							
							boolean foundFacet = false;
							for (FacetTag tag : FacetTag.values()) {
								if (currentTag.trim().equalsIgnoreCase(tag.getText())) {
									try {
										if (tagIndex != 0) {
											currentDsl
													.addDSLType(tag.getText());
										} else {
											currentDsl = new DSLTypeAndDomain(
													refID, tag.getText());
										}
									} catch (Exception e) {
										System.out.println(refID);
										e.printStackTrace();
									}
									foundFacet = true;
									break;
								}
							}
							
							if (!foundFacet) {
								subDomains.add(currentTag.trim());
							}
						}
						
						if (currentDsl == null) {
							currentDsl = new DSLTypeAndDomain(refID, "no type");
						}
						
						this.addNewDSLtoDSLTypesList(researchCellContent,
								subDomains, currentDsl);
						
					} else {
						this.numberOfIncludedNotClassifiedYet++;
					}
				} else if (selected.equalsIgnoreCase("excluded")){
					this.numberOfExcluded++;
					String tagsCellContent = row.getCell(TAGS_INDEX).getStringCellValue();
					if (tagsCellContent != null && !tagsCellContent.equals("")) {
				    	this.processCurrentTag(row, tagsCellContent, this.excludedTagsRefs);
					}
				}
			}
		}
		
		Collections.sort(this.dslTypes);
		
		if (printResult) {
			this.printTagsStatus(this.excludedTagsRefs);
			System.out.println("========================== Number of entries: "
					+ this.dslTypes.size());
			for (DSLTypeAndDomain dsl : this.dslTypes) {
				System.out.println(dsl);
			}
			System.out.println("==========================");
		}
	}

	private void addNewDSLtoDSLTypesList(String researchCellContent,
			List<String> subDomains, DSLTypeAndDomain currentDsl) {
		currentDsl.setDomains(subDomains);
		currentDsl = this.checkEmbeddedDSLAndAjust(currentDsl);
		
		//Processing research types
		String[] rTypes = researchCellContent.split(",");
		for (int j = 0; j < rTypes.length; j++) {
			currentDsl.addResearchType(rTypes[j].toLowerCase().trim());
		}
		
		Collections.sort(currentDsl.getDomains());
		this.dslTypes.add(currentDsl);
	}
	
	private DSLTypeAndDomain checkEmbeddedDSLAndAjust(DSLTypeAndDomain dsl) {
		boolean foundEmbeddedFacet = false;
		
		for(String dslType : dsl.getDslTypes()) {
			if (dslType.equalsIgnoreCase(FacetTag.EMBEDDED_DSL.getText())) {
				foundEmbeddedFacet = true;
				break;
			}
		}
		
		if (foundEmbeddedFacet) {
			List<String> newSubDomains = new ArrayList<String>();
			String technology = "";
			if (dsl.getDomains().size() > 1) {
				newSubDomains = dsl.getDomains().subList(1, dsl.getDomains().size());
				technology = dsl.getDomains().get(0);
			} else if (dsl.getDomains().size() == 1) {
				technology = dsl.getDomains().get(0);
			}
					
			EmbeddedDSL eDsl = new EmbeddedDSL(dsl.getRefID(), dsl.getDslTypes(),
					technology, newSubDomains);
			return eDsl;
		} else {
			return dsl;
		}
	}
	
	private void processCurrentTag(Row row, String tagsCellContent, Map<String, ArrayList<Integer>> refBag) {
		String[] splittedTags = tagsCellContent.split(",");
		for (int j = 0; j < splittedTags.length; j++) {
			int refID = (int) row.getCell(REF_ID_INDEX).getNumericCellValue();
			
			ArrayList<Integer> list;
			String currentKey = splittedTags[j].trim().toLowerCase();
			if (refBag.containsKey(currentKey)) {
				list = refBag.get(currentKey);
			} else {
				list = new ArrayList<Integer>();
			}
			list.add(refID);
			refBag.put(currentKey, list);
		}
	}
	
	/**
	 * Prints all tags status and their respective number of occurrences
	 * 
	 * @param refBag The references mapping (tagKey, List of references) 
	 */
	public void printTagsStatus(Map<String, ArrayList<Integer>> refBag) {
		this.printGeneralTagsStatus();
		
		Set<String> keys = refBag.keySet();
		System.out.println("Number of unique tags: " + keys.size());
		
		ArrayList<String> keysArray = new ArrayList<String> (keys);
		Collections.sort(keysArray);
		for (Iterator<String> iterator = keysArray.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			ArrayList<Integer> list = refBag.get(key);
			String listWithSemiCommas = list.toString().replace(',', ';');
			System.out.printf("%35s %3d  %s\n", key, refBag.get(key).size(), listWithSemiCommas);
		}
	}
	
	/**
	 * Print individually domain tags and their respective number of occurrences
	 */
	public void printAllUniqueDomainsAndOccurrences() {
		this.processTagsAndDomainsStatus(false);
		Map<String, Integer> domainBag = new HashMap<String, Integer>();
		for (DSLTypeAndDomain paper : this.dslTypes) {
			
			if (!paper.getDslTypes().contains("no type") ) {
				List<String> currentDomains = paper.getDomains();

				for (String tag : currentDomains) {
					if (domainBag.containsKey(tag)) {
						int temp = domainBag.get(tag);
						temp++;
						domainBag.put(tag, temp);
					} else {
						domainBag.put(tag, 1);
					}
				}
			}
		}
		
		System.out.println("====================================");
	
		Set<String> keys = domainBag.keySet();
		System.out.println("Number of unique domains: " + keys.size());
		
		ArrayList<String> keysArray = new ArrayList<String> (keys);
		Collections.sort(keysArray);
		for (Iterator<String> iterator = keysArray.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			System.out.printf("%-35s ; %3d\n", key, domainBag.get(key));
		}
	}
	
	/**
	 * This method counts and prints domain aggregations according to 
	 * {@link FacetTag} listed domains
	 *  
	 */
	public void countAndPrintDomainAggregations() {
		this.processTagsAndDomainsStatus(false);
		Map<String, Integer> domainAggregations = new TreeMap<String, Integer>();
		
		//Initializing domain keys with 0 occurrences
		for (String[] key : DomainAggregatedTags.DOMAINS) {
			domainAggregations.put(key[0].trim(), 0);
		}
		
		for (DSLTypeAndDomain paper : this.dslTypes) {
			List<String> currentDomains = paper.getDomains();

			outer: for (String currentDomain : currentDomains) {
				
				for (int i = 0; i < DomainAggregatedTags.DOMAINS.length; i++) {
					for (int j = 0; j < DomainAggregatedTags.DOMAINS[i].length; j++) {
						if (currentDomain.trim().equalsIgnoreCase(DomainAggregatedTags.DOMAINS[i][j].trim())) {
							int temp = domainAggregations.get(DomainAggregatedTags.DOMAINS[i][0].trim());
							temp++;
							domainAggregations.put(DomainAggregatedTags.DOMAINS[i][0].trim(), temp);
							continue outer;
						}
					}
				}
			}
		}
		
		Set<String> keys = domainAggregations.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			System.out.printf("%-35s ; %3d\n", key, domainAggregations.get(key));
		}
	}

	/**
	 * Prints general tag status
	 */
	public void printGeneralTagsStatus() {
		System.out.println("Total papers classified: " + 
				(this.numberOfExcluded + this.numberOfIncluded));
		System.out.println("Papers included: " + this.numberOfIncluded);
		System.out.println("Papers included and classified: " + this.numberOfIncludedAndClassified);
		System.out.println("Papers included and NOT classified: " + this.numberOfIncludedNotClassifiedYet);
	}
	
	/**
	 * This method prints the bubble chart data, with cross information between
	 * top 15 domains and their corresponding dsl research type
	 */
	public void printDomainsCrossDSLResearchTypesForBubbleChart() {
		this.processTagsAndDomainsStatus(false);
		
		Map<String, Integer> result = new HashMap<String, Integer>();
		
		for (FacetTag facet : FacetTag.values()) {
			String currentType = facet.getText();
			for (Iterator<DSLTypeAndDomain> iterator2 = this.dslTypes.iterator(); iterator2.hasNext();) {
				DSLTypeAndDomain currentPaper = iterator2.next();
				for(String dslType : currentPaper.getDslTypes()) {
					if (currentType.equalsIgnoreCase(dslType)) {
						List<String> currentDomains = currentPaper.getDomains();
						for (String currentDomain : currentDomains) {
							currentDomain = currentDomain.trim().toLowerCase();
							for (int i = 0; i < DomainAggregatedTags.TOP15_DOMAINS.length; i++) {
								for (int j = 0; j < DomainAggregatedTags.TOP15_DOMAINS[i].length; j++) {
									if (currentDomain.trim().equalsIgnoreCase(DomainAggregatedTags.TOP15_DOMAINS[i][j].trim())) {
										String key = currentType + "," + DomainAggregatedTags.TOP15_DOMAINS[i][0];
										if (result.containsKey(key)) {
											int number = result.get(key);
											number++;
											result.put(key, number);
										} else {
											result.put(key, 1);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		Set<String> keys = result.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			System.out.println(key + ";" + result.get(key));
		}
	}
	
	public static void main(String[] args) throws Exception {
		TagAnalizer ta = new TagAnalizer();
//		ta.printResearchTypeTags(true);
//		ta.processAllTags(true);
//		ta.processTagsAndDomainsStatus(true);
//		ta.printAllUniqueDomainsAndOccurrences();
//		ta.printDomainsCrossDSLResearchTypesForBubbleChart();
//		ta.countAndPrintDomainAggregations();
	}

}
