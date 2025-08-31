package studiplayer.audio;

public class NotPlayableException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotPlayableException() {
		this.pathname = "";
	}
	
	private final String pathname;
	
	public NotPlayableException(String pathname, String msg) {
		super(pathname + msg);
		this.pathname = pathname;
	}
	
	public NotPlayableException(String pathname, Throwable t) {
		super(pathname + t);
		this.pathname = pathname;
	}
	
	public NotPlayableException(String pathname, String msg, Throwable t) {
		super(pathname +  msg, t);
		this.pathname = pathname;
	}

	public String getPathname() {
		return pathname;
	}
	
	
}
