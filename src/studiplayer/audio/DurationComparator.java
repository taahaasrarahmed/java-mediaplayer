package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile>{

	public DurationComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(AudioFile a1, AudioFile a2) {
		if (a1.getDuration() > a2.getDuration()) {
			return 1;
		}
		
		else if (a1.getDuration() < a2.getDuration()) {
			return -1;
		}
		
		else {
			return 0;
		}
	}
}