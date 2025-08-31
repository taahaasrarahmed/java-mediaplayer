package studiplayer.audio;

import java.io.File;
public abstract class AudioFile {
	private String pathname;
	private String filename;
	private String author;
	private String title;
	protected long duration;
	public abstract void play() throws NotPlayableException;
	public abstract void togglePause();
	public abstract void stop();
	public abstract String formatPosition();
	public abstract String formatDuration();
	

	//Constructor
	public AudioFile(){
		this.pathname="";
		this.filename="";
		this.author="";
		this.title="";
	}
	
	public AudioFile (String path) throws NotPlayableException {
		parsePathname(path);
		
		//added
		File f = new File(this.pathname);
		if (!f.canRead()) {
		    throw new NotPlayableException(path, "The path is unreadable!");
		}
		//added
		
		parseFilename(this.filename);
//		this.author = "";
//		this.title = "";
	}


//Checks whether program is being run on windows or non-windows
private boolean isWindows() { 
	return System.getProperty("os.name").toLowerCase() 
			.indexOf("win") >= 0; 
}

	
	//This function takes an input path, and extracts pathname and filename that are stored after applying rules
	public void parsePathname(String path) {
		String pathDoublesRemoved = "";  //empty string that will contain non repeated symbols
		
		path = path.replace("‿", "").trim(); //this removes all the space symbols
		
		if ( path.length() >= 2 && path.charAt(1) == ':' && !isWindows()) {
			path = "/" + path.charAt(0) + path.substring(2); //this adjusts the path format for windows/not-windows
		}
		
	    if (isWindows()) {
	        path = path.replace("/", "\\").trim();  //convert forward slashes into back slashes
	    }  
	    else {
	        path = path.replace("\\", "/").trim();  //convert back slashes into forward slashes
	    }
		
	    for (int i = 0; i < path.length(); i++) {
	        char currentChar = path.charAt(i);
	        
	        if ((currentChar == '/' || currentChar == '\\') && pathDoublesRemoved.endsWith(String.valueOf(currentChar))) {
	            continue; //continue if characters are duplicate
	        }

	        pathDoublesRemoved += currentChar;  
	     
	    }
	    this.pathname = pathDoublesRemoved.trim();
	    
	    
	    int lastSlashIndex = pathDoublesRemoved.lastIndexOf("/"); //checks for the last occurrence of slash
	    if (lastSlashIndex == -1) { //if no forward slash is found
	        lastSlashIndex = pathDoublesRemoved.lastIndexOf("\\"); //look for back slash
	    }

	    if (lastSlashIndex != -1) {
	        this.filename = pathDoublesRemoved.substring(lastSlashIndex + 1).trim();  //everything after the last slash
	    } else {
	        this.filename = pathDoublesRemoved.trim();  //if no slash is found, the whole path is the filename
	    }
	}
	
	public void parseFilename(String file) { //this will separate the song names and the artist names from the file
//		int firstIndexAuthor = file.indexOf("‿-‿");
		if (file.contains(" - ") && file.length()>3) {
			this.author = file.substring(0, file.indexOf(" - ")).trim();
			this.title = file.substring(file.indexOf(" - ") + 2, file.lastIndexOf('.')).trim();
		}
		else if (file.equals("-")) {
			this.title="-";
			this.author = "";
				
			}
		else if (file.startsWith(".")){
			this.title = "";
			this.author = "";
			}
		
	    else if (file.lastIndexOf('.') > 0) {
	        int dot = file.lastIndexOf('.');
	        this.title  = file.substring(0, dot).trim();
	        this.author = "";
	    }
		else {
			this.title = "";
			this.author = "";
		}
		}
		
		
	
	
	public String getPathname() {
	    return this.pathname;  //getter for pathname
	}
	
	
	public String getFilename() {
		return this.filename; //getter for filename
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
	    this.title = title;
	}

	public void setAuthor(String author) {
	    this.author = author;
	}
	
	public String toString() {
		if (getAuthor() == null || getAuthor() == ""){
			return this.title;
		}
		else {
			return getAuthor() + " - " + getTitle(); 
			}
	}
	
	public long getDuration() {
	    return duration;
	}


	}
	
	