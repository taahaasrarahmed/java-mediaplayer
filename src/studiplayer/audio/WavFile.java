package studiplayer.audio;

import studiplayer.basic.WavParamReader;
public class WavFile extends SampledFile{
	
	
    public WavFile() {
        super();
    }

    public WavFile(String path) throws NotPlayableException {
    	super(path);
    	readAndSetDurationFromFile(); //initiates the moment a WAV file is created
    }

    public void readAndSetDurationFromFile() throws NotPlayableException { //static methods of the WavParamReader class imported
    	try {
    		WavParamReader.readParams(getPathname()); //reads meta data
    	} catch (Exception e) {
			throw new NotPlayableException(getPathname(), "Tag of the WAV File is not readable!");
    	}
    	
    	float frameRate   = WavParamReader.getFrameRate(); //stores metadata
    	long  numberOfFrames = WavParamReader.getNumberOfFrames(); //stores metadata
    	this.duration = computeDuration(numberOfFrames, frameRate);
    }
    

    public String toString() {
    	return super.toString() + " - " + formatDuration();
    	}
 
    
    public long getDuration() {
    	return duration;
    }

    // duration = number of frames / frame rate
    public static long computeDuration(long numberOfFrames, float frameRate) {
        return (long)(numberOfFrames * 1_000_000L / frameRate); //multiplied since required value is in ms instead of seconds
    }
}

