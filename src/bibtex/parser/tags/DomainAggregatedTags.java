package bibtex.parser.tags;

public interface DomainAggregatedTags {
	
	public static final String[] WEB = {
		"web", "web services", "semantic web", "adaptive web systems", "web apis",  
		"services mashup", "rest", "SLA", "service level agreement", "semantic web",                        
		"service oriented systems", "soa", "internet telephony",
		"service orchestration", "services orchestration", "ws-bpel", "services composition"
	};
	
	public static final String[] EMBEDDED = {
		"embedded systems"		
	};
	
	public static final String[] LOW_LEVEL_SOFTWARE = {
		"low-level software", "device drivers", "operating system", "pipeline", 
		"resource-constrained devices", "application scheduling", 
		"constraint programming"                            
	};
	  
	public static final String[] CONTROL_SYSTEMS = {
		"control systems","railway control systems", "train interlocking",
		"automotive systems", "naval", "lighting", "stage lighting", "avionic", 
		"flight control", "vehicle traffic"
	};
	
	public static final String[] PARALLEL_COMPUTING = {
		"parallel computing", "high performance computing", "multi threading", 
		"multi-core platform", "multitask systems"
	};
	  
	public static final String[] SIMULATION = {
		"simulation", "ants simulation", "urban simulation", "space simulation"
	};
	
	public static final String[] DATA_INTESENIVE_APPS = {
		"database", "query language", "sql-like language", "data processing",
		"data acquisitions", "data consistency", "data evolution", "data flow", 
		"data mapping", "data validation", "data visualization", 
		"decision support system", "data description", "data integration", 
		"data analysis", "data structures", "data definition language", 
		"data transformation", "data translation"                    

	};
	
	public static final String[] REAL_TIME = {
		"real time systems"		
	};
	
	public static final String[] SECURITY = {
		"security", "security policies", "access control", "cryptography"
	};
	
	public static final String[] HW_DESCRIPTION = {
		"hardware description", "hardware verification", "hardware sofware co-design"		
	};
	
	public static final String[] DYNAMIC_SYSTEMS = {
		"dynamic systems", "context-aware systems", "ambient intelligence", 
		"autonomic systems", "adaptive systems", "reconfiguration", "self adaptative systems"	
	};
	
	public static final String[] TESTS = {
		"testing", "test automation", "test code generation", "testbeds",    
		"performance tests", "spl testing", "domain testing", "dsl testing",
		"language testing", 
	};
	
	public static final String[] MOBILE_COMPUTING = {
		"mobile apps", "mobile computing", "mobile networks", "mobile telephony",
		"iphone", "android"
	};
	
	public static final String[] AUTOMATION = {
		"automation", "home automation", "kiosks"
	};
	
	public static final String[] BIOINFORMATICS = {
		"bioinformatics", "biology", "biomedical"
	};
	
	public static final String[] NETWORK_DISTRIBUTED_SYSTEMS = {
		"network", "network management", "networked environment", 
		"active networks", "sensor networks", "communication protocols", 
		"distributed systems", "router configuration", "router specification", 
		"routing", "packet processing", "telecommunication" , "communication"                            
	};
	
	public static final String[] GAMES = {
		"games", "game design", "game theory"
	};
	
	public static final String[] EDUCATION = {
		"education"
	};
	
	public static final String[] MULTI_AGENT_SYSTEMS = {
		"multi-agent systems"
	};
	
	public static final String[] ROBOTICS = {
		"robotics"
	};
	
	public static final String[] HEALTHCARE = {
		"healthcare"
	};
	
	public static final String[] MULTIMEDIA = {
		"multimedia", "stream processing", "streaming", "hypermedia", 
		"signal processing"
	};
	
	public static final String[] CLOUD_GRID_COMPUTING = {
		"grid computing", "cloud computing"
	};
	
	public static final String[] GUI = {
		"GUI", "user interface"
	};
	
	public static final String[] REQ_ENGINEERING = {
		"requirements engineering", "i*"
	};
	
	public static final String[] GRAPHICS_IMAGE = {
		"gpu", "graphics", "image processing"
	};
	
	public static final String[] PERVASIVE_COMPUTING = {
		"pervasive computing"
	};

	public static final String[] VISUAL_LANGUAGE = {
		"visual language", "visual programming languages"	
	};
	
	public static final String[] ONTOLOGY = {
		"ontology", "domain ontologies"                   
	};
	
	public static final String[] OTHERS = {
		"chemistry", "medical", "physical systems", "e-commerce", "engineering", "math",
		"soccer", "sports", "publishing", "logging", "geometry", "ecology", 
		"electrical engineering", "text processing", "graphs",           
		"algorithms", "compliance", "digital forensics", "business logic", 
		"finance", "animation", "multimedia animation"
	};

	public static String[][] DOMAINS = {
		WEB, EMBEDDED, LOW_LEVEL_SOFTWARE,
		CONTROL_SYSTEMS, PARALLEL_COMPUTING, SIMULATION, DATA_INTESENIVE_APPS,
		REAL_TIME, SECURITY, HW_DESCRIPTION, DYNAMIC_SYSTEMS, TESTS,
		MOBILE_COMPUTING, AUTOMATION, BIOINFORMATICS, NETWORK_DISTRIBUTED_SYSTEMS,
		GAMES, EDUCATION, MULTI_AGENT_SYSTEMS, ROBOTICS, HEALTHCARE, MULTIMEDIA,
		CLOUD_GRID_COMPUTING, GUI, REQ_ENGINEERING, GRAPHICS_IMAGE, 
		PERVASIVE_COMPUTING, VISUAL_LANGUAGE, ONTOLOGY, OTHERS
	};

	public static String [][] TOP15_DOMAINS = {
		WEB, NETWORK_DISTRIBUTED_SYSTEMS, DATA_INTESENIVE_APPS, CONTROL_SYSTEMS, 
		LOW_LEVEL_SOFTWARE, PARALLEL_COMPUTING, VISUAL_LANGUAGE, EMBEDDED, OTHERS, 
		TESTS, SECURITY, DYNAMIC_SYSTEMS, REAL_TIME, SIMULATION, EDUCATION
	};
	
}
