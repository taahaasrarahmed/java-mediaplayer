package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile>{

	public TitleComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		if (o1 == null || o2 == null) {
			throw new RuntimeException("null values are not allowed!");
			}
		return o1.getTitle().compareTo(o2.getTitle());
	}
}
