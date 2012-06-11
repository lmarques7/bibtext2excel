package bibtex.parser.tags;

import java.util.List;

public class EmbeddedDSL extends DSLTypeAndDomain {

	private String technology;
	
	public EmbeddedDSL(int refID, List<String> dslType, String technology, List<String> domains) {
		super(refID, dslType, domains);
		this.technology = technology;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}
	
	@Override
	public String toString() {
		String formattedDomains = this.getDomains().toString();
		return String.format("%6d %15s[TECHNOLOGY: %15s]-%s", 
				this.getRefID(), this.getDslTypes(), this.technology, formattedDomains);
	}
	
	@Override
	public int compareTo(DSLTypeAndDomain next) {
		int result;
		if (this.getDslTypes().equals(next.getDslTypes())) {
			EmbeddedDSL tempNext = (EmbeddedDSL) next;
			if (this.technology.equalsIgnoreCase(tempNext.getTechnology())) {
				result = this.compareTwoLists(this.getDomains(), next.getDomains());	
			} else {
				result = this.technology.compareTo(tempNext.getTechnology());
			}
			
		} else {
			result = this.compareTwoLists(this.getDslTypes(), next.getDslTypes());
		}
		return result;
	}
}
