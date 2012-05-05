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

	public void processAllTags() {
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
		this.printTagsStatus(this.tagMapRefs);
	}

	public void processTagsAndDomainsStatus() {
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
						for (Iterator<String> iterator = splittedTags.iterator(); iterator.hasNext();) {
							String currentTag = iterator.next();
							
							boolean foundFacet = false;
							for (FacetTag tag : FacetTag.values()) {
								if (currentTag.equalsIgnoreCase(tag.getText())) {
									if (currentDsl != null) {
										currentDsl.setDomains(subDomains);										
									} 
									currentDsl = new DSLTypeAndDomain(refID, tag.getText());
									foundFacet = true;
									break;
								}
							}
							
							if (foundFacet) {
								subDomains = new ArrayList<String>();
							} else {
								subDomains.add(currentTag.trim());
							}
						}
						
						if (currentDsl == null) {
							currentDsl = new DSLTypeAndDomain(refID, "no type");
						}
						
						currentDsl.setDomains(subDomains);
						currentDsl = this.checkEmbeddedDSLAndAjust(currentDsl);
						
						//Processing research types
						String[] rTypes = researchCellContent.split(",");
						for (int j = 0; j < rTypes.length; j++) {
							currentDsl.addResearchType(rTypes[j].toLowerCase().trim());
						}
						
						Collections.sort(currentDsl.getDomains());
						this.dslTypes.add(currentDsl);
						
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
		
		this.printTagsStatus(this.excludedTagsRefs);
		System.out.println("========================== Number of entries: " + this.dslTypes.size());
		for (DSLTypeAndDomain dsl : this.dslTypes) {
			System.out.println(dsl);
		}
		System.out.println("==========================");
	}
	
	private DSLTypeAndDomain checkEmbeddedDSLAndAjust(DSLTypeAndDomain dsl) {
		if (dsl.getType().equalsIgnoreCase(FacetTag.EMBEDDED_DSL.getText())) {
			List<String> newSubDomains = dsl.getDomains().subList(1, dsl.getDomains().size());
			EmbeddedDSL eDsl = new EmbeddedDSL(dsl.getRefID(), dsl.getType(), 
					dsl.getDomains().get(0), newSubDomains);
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
			if (refBag.containsKey(splittedTags[j].trim().toLowerCase())) {
				list = refBag.get(splittedTags[j].trim().toLowerCase());
			} else {
				list = new ArrayList<Integer>();
			}
			list.add(refID);
			refBag.put(splittedTags[j].trim().toLowerCase(), list);
		}
	}
	
	public void printTagsStatus(Map<String, ArrayList<Integer>> refBag) {
		this.printGeneralTagsStatus();
		
		Set<String> keys = refBag.keySet();
		System.out.println("Number of unique tags: " + keys.size());
		
		ArrayList<String> keysArray = new ArrayList<String> (keys);
		Collections.sort(keysArray);
		for (Iterator<String> iterator = keysArray.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			System.out.printf("%35s %3d   ", key, refBag.get(key).size());
			ArrayList<Integer> list = refBag.get(key);
			System.out.println(list);
		}
	}
	
	public void processUniqueDomains() {
		Map<String, Integer> domainBag = new HashMap<String, Integer>();
		for (DSLTypeAndDomain paper : this.dslTypes) {
			
			if (paper.getType().equals(FacetTag.EMBEDDED_DSL.getText()) ||
				paper.getType().equals(FacetTag.EXTERNAL_DSL.getText()) ||
				paper.getType().equals(FacetTag.DSAL.getText())||
				paper.getType().equals(FacetTag.DSML.getText()) ||
				paper.getType().equals(FacetTag.ADL.getText()) ) {
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
			
			if (domainBag.get(key) > 10) {
				System.out.printf("%35s ; %3d\n", key, domainBag.get(key));
			}
		}
	}

	public void printGeneralTagsStatus() {
		System.out.println("Total papers classified: " + 
				(this.numberOfExcluded + this.numberOfIncluded));
		System.out.println("Papers included: " + this.numberOfIncluded);
		System.out.println("Papers included and classified: " + this.numberOfIncludedAndClassified);
		System.out.println("Papers included and NOT classified: " + this.numberOfIncludedNotClassifiedYet);
	}
	
	public void printDomainsVSTypes() {
		ArrayList<String> types = new ArrayList<String>();
		
		types.add(FacetTag.EMBEDDED_DSL.getText());
		types.add(FacetTag.EXTERNAL_DSL.getText());
		types.add(FacetTag.DSML.getText());
		types.add(FacetTag.TECHNIQUE.getText());
		types.add(FacetTag.PROCESS.getText());
		types.add(FacetTag.METHOD.getText());
		types.add(FacetTag.TOOLS.getText());
		
		Map<String, Integer> result = new HashMap<String, Integer>();
		
		for (Iterator<String> iterator = types.iterator(); iterator.hasNext();) {
			String currentType = iterator.next();
			for (Iterator<DSLTypeAndDomain> iterator2 = this.dslTypes.iterator(); iterator2.hasNext();) {
				DSLTypeAndDomain currentPaper = iterator2.next();
				if (currentType.equalsIgnoreCase(currentPaper.getType())) {
					List<String> currentDomains = currentPaper.getDomains();
					for (String currentDomain : currentDomains) {
						currentDomain = currentDomain.trim().toLowerCase();
						for (int i = 0; i < Top10Domains.DOMAINS.length; i++) {
							for (int j = 0; j < Top10Domains.DOMAINS[i].length; j++) {
								if (currentDomain.equalsIgnoreCase(Top10Domains.DOMAINS[i][j])) {
									String key = currentType + "," + Top10Domains.DOMAINS[i][0];
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
		
		Set<String> keys = result.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			System.out.println(key + ";" + result.get(key));
		}
	}
	
	public static void main(String[] args) throws Exception {
		TagAnalizer ta = new TagAnalizer();
//		ta.processAllTags();
//		ta.processUniqueDomains();
		ta.processTagsAndDomainsStatus();
//		ta.printDomainsVSTypes();
		

		
	}

}
