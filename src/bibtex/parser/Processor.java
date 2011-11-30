package bibtex.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Processor {
	
	public static final String BIB_HOME = "./Arquivos";
	public static final String SHEET_FILE = BIB_HOME + "/Artigos.xls";

	public static void main(String[] args) throws IOException {
		File diretorio = new File(BIB_HOME);
		File listaPastas[] = diretorio.listFiles();
		ExcelSheetGenerator p = new ExcelSheetGenerator(SHEET_FILE);
		
		for (int i = 0; i < listaPastas.length; i++) {
			if(!listaPastas[i].isDirectory()) { 
				// Se não é subpasta, continua com o laço
				continue;
			}
			File listaArquivos[] = listaPastas[i].listFiles(new BibFileFilter());
			ArrayList<Manuscript> listaArtigos = new ArrayList<Manuscript>();
			// Pega todos os arquivos do diretório
			for (File arquivo : listaArquivos) {
				Manuscript artigo = processFile(arquivo, listaArtigos);
				if (artigo != null) {
					// Adicionando o último artigo processado
					listaArtigos.add(artigo);
				}
			}
			int ultIndice = listaPastas[i].getCanonicalPath().lastIndexOf("\\");
			String nomePasta = listaPastas[i].getCanonicalPath().substring(ultIndice + 1); 
			System.out.println("Artigos processados de " + nomePasta + ": " +
												 listaArtigos.size());
			p.parse(listaArtigos, nomePasta);
		}
	}

	private static Manuscript processFile(File arquivo, ArrayList<Manuscript> listaArtigos) {
		String resultado;
		Manuscript artigo = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(arquivo));
			String linha;
			while (in.ready()) {
				linha = in.readLine().trim().toLowerCase();

				if (linha.startsWith("@")) {
					// Adiciona o paper previamente gerado na lista
					
					if (artigo !=  null) {
						listaArtigos.add(artigo);
					}
					artigo = new Manuscript();
				}
				
				if (artigo != null) {
					for (BibTag bibTag : BibTag.values()) {
						resultado = processTag(bibTag.getTag(), linha, in);
						if (resultado != null) {
							switch (bibTag) {
								case AUTHOR:
									List<String> authorsList = processAuthorsTag(resultado);
									artigo.setAuthors(authorsList);
									break;
								case TITLE:
									artigo.setTitle(resultado);
									break;
								case BOOK_TITLE:
									artigo.setConference(resultado);
									break;
								case JOURNAL:
									artigo.setJournal(resultado);
									break;
								case YEAR:
									artigo.setYear(resultado);
									break;
								case ABSTRACT:
									artigo.setAbstract(resultado);
									break;
							}
						}
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return artigo;
	}
	
	private static List<String> processAuthorsTag(String resultado) {
		ArrayList<String> autores = new ArrayList<String>();
		if (resultado != null) {
			String[] nomes;
			if (resultado.contains(",,")) {
				resultado = resultado.replace(",,", ",");
			}
			if (resultado.contains(" and ")) {
				nomes = resultado.split(" and ");
				for (String nome : nomes) {
					autores.add(nome.trim());
				}
			} else {
				nomes = resultado.split(",");
				for (int i = 0; i < nomes.length; i++) {
					String lastName = nomes[i].trim();
					i++;
					if (i < nomes.length) {
						String fullName = lastName + ", " + nomes[i].trim();	
						autores.add(fullName);
					}
					
				}
			}
		}
		return autores;
	}

	private static String processTag(String tag, String line, BufferedReader in) throws IOException {
		String resultado = null;
		if (line.startsWith(tag + "={") || line.startsWith(tag + " = {")) {
			line = line.replaceAll(" = \\{", "={");
			tag = tag + "={"; // As chaves marcam o início da tag no bibtex
			int inicio = line.indexOf(tag) + tag.length();
			int fim = line.indexOf("}", inicio);
			while (fim < 0) { // Executa o laço até encontrar o '}' para marcar o fim da tag
				String nextLine = in.readLine();
				line += (" " + nextLine.trim());
				fim = line.indexOf("}", inicio);
			}
			resultado = line.substring(inicio, fim).trim();
		}
		return resultado;
	}

}
