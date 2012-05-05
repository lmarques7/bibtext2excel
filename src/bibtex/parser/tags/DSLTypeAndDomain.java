package bibtex.parser.tags;

import java.util.ArrayList;
import java.util.List;

public class DSLTypeAndDomain implements Comparable<DSLTypeAndDomain>{
	
	private int refID;
	private String type;
	private List<String> domains;
	private List<String> researchTypes;
	
	public DSLTypeAndDomain(int refID, String type, List<String> domains) {
		this(refID, type);
		this.domains = domains;
	}
	
	public DSLTypeAndDomain(int refID, String type) {
		this.refID = refID;
		this.type = type;
		this.domains = new ArrayList<String>();
		this.researchTypes = new ArrayList<String>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getDomains() {
		return domains;
	}
	
	public void setDomains(List<String> domains) {
		this.domains = domains;
	}

	public void addDomains(String newDomain) {
		this.domains.add(newDomain);
	}
	
	public List<String> getResearchTypes() {
		return researchTypes;
	}

	public void setResearchTypes(List<String> researchTypes) {
		this.researchTypes = researchTypes;
	}
	
	public void addResearchType(String researchType) {
		this.researchTypes.add(researchType);		
	}

	public int getRefID() {
		return refID;
	}

	public void setRefID(int refID) {
		this.refID = refID;
	}

	@Override
	public String toString() {
		String formattedDomains = this.domains.toString();
		return String.format("%6d [%15s]-%s", this.refID, this.type, formattedDomains);
	}

	@Override
	public int compareTo(DSLTypeAndDomain next) {
		int result;
		if (this.type.equals(next.getType())) {
			result = this.compareTwoLists(this.domains, next.domains);
		} else {
			result = this.type.compareTo(next.getType());
		}
		return result;
	}
	
	protected int compareTwoLists(List<String> a1, List<String> a2) {
		int result = 0;
		if (a1.size() > a2.size()) {
			for (int i = 0; i < a2.size(); i++) {
				result = a1.get(i).compareTo(a2.get(i));
				if (result != 0) {
					break;
				} 
			}
		} else {
			for (int i = 0; i < a1.size(); i++) {
				result = a1.get(i).compareTo(a2.get(i));
				if (result != 0) {
					break;
				}
			}
		}
		return result;
	}

}
