package bibtex.parser.tags;

public enum ResearchTypeTag {

    EXPERIENCE ("experience paper"),
    OPINION ("opinion paper"),
    PHILOSOPHICAL ("philosophical paper"),
    SOLUTION ("solution proposal"),
    VALIDATION ("validation research"),
    EVALUATION ("evaluation research");
    
    private ResearchTypeTag(String text) {
    	this.text = text;
	}
    
	private final String text;

	public String getText() {
		return text;
	}
	
}
