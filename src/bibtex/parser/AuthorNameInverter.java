package bibtex.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AuthorNameInverter {

	public static void main(String[] args) throws IOException {
		File input = new File("C:\\Users\\Leandro\\Documents\\input.txt");
		File output = new File("C:\\Users\\Leandro\\Documents\\output.txt");
		output.createNewFile();

		BufferedReader reader = new BufferedReader(new FileReader(input));
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] names = line.split(" ");
			String fullName = names[names.length - 1] + ",";
			for (int i = 0; i < names.length - 1; i++) {
				fullName = fullName + " " + names[i].trim();				
			}
			writer.write(fullName + "\n");
		}

		writer.close();
	}

}
