package studiplayer.audio;

import studiplayer.basic.TagReader;
import java.util.Map;  
public class TaggedFile extends SampledFile {
    private String album;

	public TaggedFile() {
		super();
	}
	
	public TaggedFile(String path) throws NotPlayableException {
		super(path);
		readAndStoreTags();	//implements album and duration 
	}
	
	public String getAlbum() {
		return album;
	}
	
	
	public void readAndStoreTags() throws NotPlayableException {
		Map<String,Object> tagMap;
		
		try{
			tagMap= TagReader.readTags(getPathname());
		}catch (Exception e){
			throw new NotPlayableException(getPathname(), "Could not read Tags") ;
		}
		
		//instead of for loop, we will create objects that are needed and use get(name) to get our key/value pairs
		Object tObj = tagMap.get("title");
		if (tObj != null) {
			setTitle(tObj.toString().trim());
		}

		Object aObj = tagMap.get("author");
		if (aObj != null) {
			setAuthor(aObj.toString().trim());
		}

		Object albObj = tagMap.get("album");
		if (albObj != null) {
			album = albObj.toString().trim();
		}

		Object dObj = tagMap.get("duration");
		if (dObj != null) {
			duration = ((Number)dObj).longValue();
		}
	    
	        
	}
	
	public String toString() {
	    String result = super.toString();
	    String formattedDuration = formatDuration();
	    if (album != null && !album.isEmpty()) {
	        return result + " - " + album + " - " + formattedDuration;
	    }
	    return result + " - " + formattedDuration;
	}

	}

