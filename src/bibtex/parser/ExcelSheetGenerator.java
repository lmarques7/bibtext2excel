package bibtex.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelSheetGenerator {
	
	// Constantes
	private static final int ID_INICIAL = 0; 
	
	private String arquivoPlanilha;
	private int id = ID_INICIAL;
	HSSFWorkbook workbook = new HSSFWorkbook();

	public ExcelSheetGenerator(String localPlanilha) {
		this.arquivoPlanilha = localPlanilha;
	}
	
	public void parse(List<Manuscript> listaArtigos, String searchEngineName) {

		Sheet manuscriptSheet = this.workbook.createSheet(searchEngineName);
		Sheet authorSheet = this.workbook.createSheet(searchEngineName + " - authors");

		int linhaArtigos = 0, linhaAutores = 0;

		createManuscriptHeader(manuscriptSheet, linhaArtigos);
		createAuthorsHeader(authorSheet, linhaAutores);
		linhaArtigos++;
		linhaAutores++;

		List<String> autores;
		for (int i = 0; i < listaArtigos.size(); i++) {

			Row manuscriptRow = manuscriptSheet.createRow(linhaArtigos);

			Cell cell = manuscriptRow.createCell(0);
			cell.setCellValue(id); // Número sequencial do paper

			cell = manuscriptRow.createCell(1); // Título
			cell.setCellValue(listaArtigos.get(i).getTitle());

			cell = manuscriptRow.createCell(2); // Ano
			cell.setCellValue(listaArtigos.get(i).getYear());

			cell = manuscriptRow.createCell(3); // Conferência
			cell.setCellValue(listaArtigos.get(i).getConference());

			cell = manuscriptRow.createCell(4); // Journal
			cell.setCellValue(listaArtigos.get(i).getJournal());
			
			cell = manuscriptRow.createCell(5); // Abstract
			cell.setCellValue(listaArtigos.get(i).getAbstract());

			autores = listaArtigos.get(i).getAuthors();
			if (autores != null && autores.size() > 0) {
				for (String autor : autores) {
					Row authorRow = authorSheet.createRow(linhaAutores);
					cell = authorRow.createCell(0);
					cell.setCellValue(id);
					cell = authorRow.createCell(1);
					cell.setCellValue(autor.trim());
					linhaAutores++; // Colocando os autores nas próximas linhas nome do artigo
				}
			}
			linhaArtigos++;
			id++;
		}
		save(this.workbook);
	}

	private void createManuscriptHeader(Sheet sheet, int linhaArtigos) {
		Row linhaCabecalho = sheet.createRow(linhaArtigos);
		Cell cell = linhaCabecalho.createCell(0);
		cell.setCellValue("Id");
		cell = linhaCabecalho.createCell(1);
		cell.setCellValue(BibTag.TITLE.getTag());
		cell = linhaCabecalho.createCell(2);
		cell.setCellValue(BibTag.YEAR.getTag());
		cell = linhaCabecalho.createCell(3);
		cell.setCellValue(BibTag.BOOK_TITLE.getTag());
		cell = linhaCabecalho.createCell(4);
		cell.setCellValue(BibTag.JOURNAL.getTag());
		cell = linhaCabecalho.createCell(5);
		cell.setCellValue(BibTag.ABSTRACT.getTag());
	}
	
	private void createAuthorsHeader(Sheet sheet, int linhaAutores) {
		Row linhaCabecalho = sheet.createRow(linhaAutores);
		Cell cell = linhaCabecalho.createCell(0);
		cell.setCellValue("Id");
		cell = linhaCabecalho.createCell(1);
		cell.setCellValue(BibTag.AUTHOR.getTag());
	}

	private void save(Workbook wb) {

		try {
			File sheetFile = new File(arquivoPlanilha);
			if (!sheetFile.exists()) {
				sheetFile.createNewFile();
			}
			FileOutputStream fileOut = new FileOutputStream(sheetFile);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
