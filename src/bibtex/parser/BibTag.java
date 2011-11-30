package bibtex.parser;

public enum BibTag {
	AUTHOR("author"), TITLE("title"), BOOK_TITLE("booktitle"), JOURNAL("journal"),
	YEAR("year"), ABSTRACT("abstract");
	
	private String tag;
	BibTag(String tag) {
		this.tag = tag;
	}
	
	public String getTag() {
		return tag;
	}
}