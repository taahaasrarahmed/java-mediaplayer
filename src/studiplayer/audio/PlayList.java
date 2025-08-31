package studiplayer.audio;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.lang.Iterable;

public class PlayList implements Iterable<AudioFile> {
	
	private final List<AudioFile> list = new LinkedList<>();
	private ControllablePlayListIterator iterator = new ControllablePlayListIterator(list); //added iterator that will work in place of the old system
	protected String search;
	private AudioFile currentFile;
	protected AudioFile file;
	private SortCriterion sortCriterion = SortCriterion.DEFAULT;

	//will reset iterator everytime the sequence changes so our index updates
	
	public PlayList() {
		resetIteration(); 
	}
	
	public PlayList(String m3uPathname) {
	    loadFromM3U(m3uPathname); 
	}

	
	public void add(AudioFile file) {
		list.add(file);
		resetIteration();
	}
	
	public void remove(AudioFile file) {
		list.remove(file);
		resetIteration();
	}
	
	public int size() {
		return list.size();
	}
	
	public AudioFile currentAudioFile() {
		return currentFile; 
		}
	
	public void nextSong() {
		if (iterator.hasNext()) {
		    currentFile = iterator.next();
		} else {
		    resetIteration();
		}
	}
	
	//CONCEPT AS COPIED FROM TextFileIO
	public void loadFromM3U(String pathname) {
		getList().clear();
//		this.current = 0;
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(new File(pathname));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty() || line.startsWith("#")) {
					continue;
				}
				else {
					try {
						getList().add(AudioFileFactory.createAudioFile(line));
					}
					catch (Exception e) {

					}
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		} 
		finally {
			try {
				scanner.close();
			}
			catch (Exception e) {
			}
		}
		resetIteration();
	}
	
	// AS COPIED FROM TextFileIO
	public void saveAsM3U(String pathname) {
		FileWriter writer = null;
		String sep = System.getProperty("line.separator");
		
		try {
			writer = new FileWriter(pathname);
			for (AudioFile line : getList()) {
				writer.write(line.getPathname() + sep);
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to write file " + pathname + "!");
		} finally {
			try {
				System.out.println("File " + pathname + " written!");
				writer.close();
			} catch (Exception e) {
			}
		}
	}
	
	public List<AudioFile> getList(){
		return list;
	}
	

	@Override
	public Iterator<AudioFile> iterator() {
		return new ControllablePlayListIterator(getList(), getSearch(), getSortCriterion());
	}
	
	
	public String getSearch() {
		return search;
	}
	
	public void setSearch(String search) {
		this.search = search;
		resetIteration();
	}
	
	 public void jumpToAudioFile(AudioFile file) {
		 resetIteration();
		while (iterator.hasNext()) {
			 AudioFile f = iterator.next();
			 if (f.equals(file)) {
			     currentFile = f;
			     return;
			 }
		}
	 }

	public void setSortCriterion(SortCriterion setCrit) {
		this.sortCriterion = setCrit;
		resetIteration();
		
	}
	
	public String toString() {
	    return list.toString();
	}
	
	public SortCriterion getSortCriterion() {
		return this.sortCriterion;
	}
	
	private void resetIteration() {
	    iterator = new ControllablePlayListIterator(list, search, sortCriterion);
	    if (iterator.hasNext()) {
	        currentFile = iterator.next();
	    } else {
	        currentFile = null;
	    }
	}
	
	
}

	
	
