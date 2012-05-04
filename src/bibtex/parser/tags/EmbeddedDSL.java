package bibtex.parser.tags;

import java.util.List;

public class EmbeddedDSL extends DSLTypeAndDomain {

	private String technology;
	
	public EmbeddedDSL(int refID, String type, String technology, List<String> domains) {
		super(refID, type, domains);
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
		return String.format("%6d [%15s][TECHNOLOGY: %15s]-%s", 
				this.getRefID(), this.getType(), this.technology, formattedDomains);
	}
	
	@Override
	public int compareTo(DSLTypeAndDomain next) {
		int result;
		if (this.getType().equals(next.getType())) {
			EmbeddedDSL tempNext = (EmbeddedDSL) next;
			if (this.technology.equalsIgnoreCase(tempNext.getTechnology())) {
				result = this.compareTwoLists(this.getDomains(), next.getDomains());	
			} else {
				result = this.technology.compareTo(tempNext.getTechnology());
			}
			
		} else {
			result = this.getType().compareTo(next.getType());
		}
		return result;
	}
}
