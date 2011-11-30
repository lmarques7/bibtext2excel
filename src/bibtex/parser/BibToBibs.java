package bibtex.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

public class BibToBibs {

	public void separar(String file){
	
		String linha = "";
		int id = 10000;

		// Lendo do arquivo
		File arquivoEntrada = new File(file);
		FileWriter arquivoSaida;
		BufferedWriter saida = null;
		
		try {
			BufferedReader entrada = new BufferedReader(new FileReader(arquivoEntrada));
				while ((linha = entrada.readLine()) != null) {				
					StringTokenizer st2 = new StringTokenizer(linha, "@"); 
					while (st2.hasMoreElements()) {
						if (linha.contains("@")) {
							id++;
							arquivoSaida = new FileWriter(id + ".bib");
							saida = new BufferedWriter(arquivoSaida);
						}
						saida.write(linha);
						st2.nextToken();
						saida.newLine();
						saida.flush();
					}
				}
				saida.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

