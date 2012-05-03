package bibtex.parser.tags;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import bibtex.parser.Processor;

public class TagAnalizer {

	private static final int NUMBER_OF_PAPERS = 2595;
	private static final String FILE_PATH = Processor.BIB_HOME + "/Artigos_formatados.xlsx";
	private static final byte REF_ID_INDEX = 0;
	private static final byte SELECTED_INDEX = 7;
	private static final byte TAGS_INDEX = 8;

	private Map<String, ArrayList<Integer>> tagMapRefs;
	private Workbook workbook;
	private int numberOfIncluded;
	private int numberOfExcluded;
	private int numberOfIncludedAndClassified;
	private int numberOfIncludedNotClassifiedYet;

	public TagAnalizer() throws Exception {
		this.tagMapRefs = new HashMap<String, ArrayList<Integer>>();
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
					String tags = row.getCell(TAGS_INDEX).getStringCellValue();
					if (tags != null && !tags.trim().equals("")) {
						this.numberOfIncludedAndClassified++;
						String[] splittedTags = tags.split(",");
						for (int j = 0; j < splittedTags.length; j++) {
							int refID = (int) row.getCell(REF_ID_INDEX).getNumericCellValue();
							ArrayList<Integer> list;
							if (this.tagMapRefs.containsKey(splittedTags[j].trim().toLowerCase())) {
								list = this.tagMapRefs.get(splittedTags[j].trim().toLowerCase());
							} else {
								list = new ArrayList<Integer>();
							}
							list.add(refID);
							this.tagMapRefs.put(splittedTags[j].trim().toLowerCase(), list);
						}
					} else {
						this.numberOfIncludedNotClassifiedYet++;
					}
				} else if (selected.equalsIgnoreCase("excluded")){
					this.numberOfExcluded++;
				}
			}

		}
	}
	
	public void printTagsStatus() {
		System.out.println("Total papers classified: " + 
				(this.numberOfExcluded + this.numberOfIncluded));
		System.out.println("Papers included: " + this.numberOfIncluded);
		System.out.println("Papers included and classified: " + this.numberOfIncludedAndClassified);
		System.out.println("Papers included and NOT classified: " + this.numberOfIncludedNotClassifiedYet);
		
		Set<String> keys = this.tagMapRefs.keySet();
		System.out.println("Number of unique tags: " + keys.size());
		
		ArrayList<String> keysArray = new ArrayList<String> (keys);
		Collections.sort(keysArray);
		for (Iterator<String> iterator = keysArray.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			System.out.printf("%35s %3d   ", key, this.tagMapRefs.get(key).size());
			ArrayList<Integer> list = this.tagMapRefs.get(key);
			System.out.println(list);
		}
	}
	
	public static void main(String[] args) throws Exception {
		TagAnalizer ta = new TagAnalizer();
		ta.processAllTags();
		ta.printTagsStatus();
	}

}
