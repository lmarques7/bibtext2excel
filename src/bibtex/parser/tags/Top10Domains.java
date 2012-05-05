package bibtex.parser.tags;

public interface Top10Domains {
	
	public static final String[] WEB = {
		"web", "web services", "semantic web", "adptive web systems", "web apis",  "services mashup"		
	};
	
	public static final String[] EMBEDDED = {
		"embedded systems"		
	};
	
	public static final String[] LOW_LEVEL = {
		"low-level software", "low-level sofware"		
	};
	  
	public static final String[] CONTROL_SYSTEMS = {
		"control systems", "  railway control systems"		
	};
	
	public static final String[] PARALLEL_COMPUTING = {
		"parallel computing"		
	};
	  
	public static final String[] SIMULATION = {
		"simulation", "ants simulation", "urban simulation", "space simulation"
	};
	
	public static final String[] DATABASE = {
		"database"		
	};
	
	public static final String[] REAL_TIME = {
		"real time systems"		
	};
	
	public static final String[] SECURITY = {
		"security, security policies"		
	};
	
	public static final String[] HW_DESCRIPTION = {
		"hardware description", "hardware verification", "hardware sofware co-design"		
	};

	public static String[][] DOMAINS = {
		Top10Domains.WEB, Top10Domains.EMBEDDED, Top10Domains.LOW_LEVEL,
		Top10Domains.CONTROL_SYSTEMS, Top10Domains.PARALLEL_COMPUTING, 
		Top10Domains.SIMULATION, Top10Domains.DATABASE, Top10Domains.REAL_TIME,
		Top10Domains.SECURITY, Top10Domains.HW_DESCRIPTION
	};
	
}
