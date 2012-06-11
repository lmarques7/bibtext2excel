package bibtex.parser.tags;

import java.util.ArrayList;
import java.util.List;

public class DSLTypeAndDomain implements Comparable<DSLTypeAndDomain>{
	
	private int refID;
	private List<String> dslTypes;
	private List<String> domains;
	private List<String> researchTypes;
	
	public DSLTypeAndDomain(int refID, List<String> types, List<String> domains) {
		this(refID, types);
		this.domains = domains;
	}
	
	public DSLTypeAndDomain(int refID, List<String> types) {
		this.refID = refID;
		this.dslTypes = types;
		this.domains = new ArrayList<String>();
		this.researchTypes = new ArrayList<String>();
	}

	public DSLTypeAndDomain(int refID, String firstType) {
		this.refID = refID;
		this.dslTypes = new ArrayList<String>();
		this.dslTypes.add(firstType);
		this.domains = new ArrayList<String>();
		this.researchTypes = new ArrayList<String>();
	}
	
	public List<String> getDslTypes() {
		return dslTypes;
	}

	public void setDslTypes(List<String> dslTypes) {
		this.dslTypes = dslTypes;
	}

	public void addDSLType(String newType) {
		this.dslTypes.add(newType);
	}
	
	public List<String> getDomains() {
		return domains;
	}
	
	public void setDomains(List<String> domains) {
		this.domains = domains;
	}

	public void addDomain(String newDomain) {
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
		return String.format("%6d %15s-%s", this.refID, this.dslTypes, formattedDomains);
	}

	@Override
	public int compareTo(DSLTypeAndDomain next) {
		int result;
		if (this.dslTypes.equals(next.getDslTypes())) {
			result = this.compareTwoLists(this.domains, next.domains);
		} else {
			result = this.compareTwoLists(this.dslTypes, next.getDslTypes());
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
