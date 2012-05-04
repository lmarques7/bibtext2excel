package bibtex.parser.tags;

public enum FacetTag {
	EMBEDDED_DSL ("embedded dsl"),
	EXTERNAL_DSL ("external dsl"),
	ADL ("ADL"),
	DSML ("DSML"),
	DSAL ("DSAL"),
	TECHNIQUE ("technique"),
	PROCESS ("process"),
	METHOD ("method"),
	TOOLS ("tools");
	
	private FacetTag(String text) {
		this.text = text;
	}
	
	private final String text;

	public String getText() {
		return text;
	}
	
}