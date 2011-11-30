package bibtex.parser;

import java.util.List;

public class Manuscript {

	private String title;
	private List<String> authors;
	private String year;

	private String conference;
	private String journal;
	private String paperAbstract;

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
	}

	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public String getAbstract() {
		return paperAbstract;
	}

	public void setAbstract(String paperAbstract) {
		this.paperAbstract = paperAbstract;
	}

	@Override
	public String toString() {
		return "Manuscript: " + this.title + "\n" + this.authors;  
	}
	
}
