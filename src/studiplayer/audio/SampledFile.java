package studiplayer.audio;
import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {
	protected long duration;
	public SampledFile() {
		super();
	}
	
	public SampledFile(String path) throws NotPlayableException {
		super(path);
	}
	
	public void play() throws NotPlayableException{
		try{
			BasicPlayer.play(getPathname());
		}
		catch(Exception e) {
			throw new NotPlayableException(getPathname(), "Could not play! Error Occured");
		}
	}
	
	public void togglePause() {
		BasicPlayer.togglePause();
	}
	
	public void stop() {
		BasicPlayer.stop();
	}
	
	public String formatDuration() {
		return timeFormatter(getDuration());
	}
	
	public String formatPosition() {
		return timeFormatter(BasicPlayer.getPosition());
	}
	
	public static String timeFormatter(long timeInMicroSeconds) {
	    if (timeInMicroSeconds < 0) {
	        throw new RuntimeException("Time cannot be negative"); //throw exception for underflow
	    }
	    long Seconds = timeInMicroSeconds / 1_000_000L;
	    if (Seconds > 5999) { //overflow exception over 5999 seconds as our format is mm ss
	        throw new RuntimeException("Time cannot exceed 99:59");
	    }
	    long minutes =Seconds / 60;
	    long seconds = Seconds % 60;
	    return String.format("%02d:%02d", minutes, seconds);
	}

	
	public long getDuration() {
		return duration;
	}
	
}
