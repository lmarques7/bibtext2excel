package bibtex.parser;

import java.io.File;
import java.io.FileFilter;

public class BibFileFilter implements FileFilter {

	public boolean accept(File pathname) {
		if (pathname.getName().endsWith(".bib")) {
			return true;
		} else {
			return false;	
		}
	}

}
