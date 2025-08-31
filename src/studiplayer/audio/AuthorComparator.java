package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile>{

	public AuthorComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		if (o1 == null || o2 == null) {
			throw new RuntimeException("null values are not allowed!");
			}
		return o1.getAuthor().compareTo(o2.getAuthor());
	}

}
