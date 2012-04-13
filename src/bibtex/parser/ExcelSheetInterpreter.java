package bibtex.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelSheetInterpreter {

	private String arquivoPlanilha;
	Workbook workbook = new HSSFWorkbook();

	public ExcelSheetInterpreter(String localPlanilha) {
		this.arquivoPlanilha = "C:\\Users\\Leandro\\Downloads\\teste.xlsx";
	}
	
	public static void main(String[] args) throws IOException {
		ExcelSheetInterpreter esg = new ExcelSheetInterpreter("");
		InputStream is = new FileInputStream(new File(esg.arquivoPlanilha));
		esg.workbook = new XSSFWorkbook(is);
		Sheet sheet = esg.workbook.getSheetAt(0);       // first sheet
		
		for (int i = 0; i < 200; i++) {
			Row row = sheet.getRow(i); // third row
			Cell cell;  // fourth cell
			cell = row.getCell(7);
			System.out.println(cell.getStringCellValue());
		}
	}	
}
