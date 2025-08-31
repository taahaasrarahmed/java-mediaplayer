package studiplayer.audio;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
//import studiplayer.audio.PlayList.SortCriterion;
//import studiplayer.audio.TaggedFile;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
	private List<AudioFile> list;
	private int audioNumber = 0;
	
	public ControllablePlayListIterator(List<AudioFile> list) {
		this.list = list;
	}
	
	public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sort) {
        List<AudioFile> newList = new LinkedList<>(list);
        
        if (search == null || search.isEmpty()) { // return full list if there are no search parameters
            newList = new LinkedList<>(list);
        } 
        else {
            newList = new LinkedList<AudioFile>();
            for (AudioFile file : list) {
                if ((file.getTitle()  != null && file.getTitle().contains(search)) //if search isnt null and contains item (changed for cert failure)
                 || (file.getAuthor() != null && file.getAuthor().contains(search))
                 || (file instanceof TaggedFile
                     && ((TaggedFile)file).getAlbum() != null
                     && ((TaggedFile)file).getAlbum().contains(search))
                ) {
                    newList.add(file);
                }
            }
        }
		
		if (sort == SortCriterion.ALBUM) {
			newList.sort(new AlbumComparator());
		}
		else if (sort == SortCriterion.AUTHOR) {
			newList.sort(new AuthorComparator());
		}
		else if (sort == SortCriterion.DURATION) {
			newList.sort(new DurationComparator());
		}
		else if (sort == SortCriterion.TITLE) {
			newList.sort(new TitleComparator());
		}
		this.list = newList; 

	}
	

				
		
	
	public AudioFile jumpToAudioFile(AudioFile file) {
		return file;
	}

	
	@Override
	public boolean hasNext() {
	    return audioNumber < list.size();
	}
	
	@Override
	public AudioFile next() {
		AudioFile nextSong; 
		if (hasNext()) {
			nextSong = list.get(getAudioNumber()) ;
			audioNumber ++;
		}
		else {
			setAudioNumber(0);
			nextSong = list.get(getAudioNumber());
		}
				
		return nextSong;
	}

	public int getAudioNumber() {
		return audioNumber;
	}

	public void setAudioNumber(int audioNumber) {
		this.audioNumber = audioNumber;
	}
	
	
	
	
}

