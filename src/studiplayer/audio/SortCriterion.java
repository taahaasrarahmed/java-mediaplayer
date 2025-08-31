package studiplayer.audio;

public enum SortCriterion {
	DEFAULT("Default"),
	AUTHOR("Author"),
	TITLE("Title"),
	ALBUM("Album"),
	DURATION("Duration");


	private String sortCriterion;
	
	SortCriterion(String string){
		this.sortCriterion = string;
	}
	
	public String toString() {
		return sortCriterion;
	}

}