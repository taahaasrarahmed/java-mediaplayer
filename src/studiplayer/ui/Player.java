package studiplayer.ui;


import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;

public class Player extends Application{
	private boolean useCertPlayList = false;
	public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
	private String playlistPath;
	private PlayList playList;
	private static final String PLAYLIST_DIRECTORY = "playlists";
	private static final  String INITIAL_PLAY_TIME_LABEL = "00:00";
	private static final String NO_CURRENT_SONG = " - ";
	
	//buttons and fiels/labels are initiated here
	private Button playButton = new Button();
	private Button pauseButton = new Button();
	private Button nextButton = new Button();
	private Button stopButton = new Button();
	private Button filterButton = new Button("Display");
	private TextField searchTextField = new TextField();
	private Label playListLabel = new Label(PLAYLIST_DIRECTORY);
	private Label playTimeLabel = new Label(INITIAL_PLAY_TIME_LABEL);
	private Label currentSongLabel = new Label(NO_CURRENT_SONG);
	private ChoiceBox<SortCriterion> sortChoiceBox = new ChoiceBox<SortCriterion>();
	private SongTable songList;

	

	public Player() {
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void start(Stage stage) throws Exception {
		if (useCertPlayList) {
			loadPlayList(DEFAULT_PLAYLIST);
		}
		else {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(stage);
			if(file != null) {
				loadPlayList(file.getPath());
			}else {
				loadPlayList(null);
			}
				
		}
		
		
		BorderPane mainPane = new BorderPane();
		stage.setTitle("APA Player");

		
		

			//Layout
		
			// Top area 
			TitledPane filterPane = new TitledPane();
			GridPane filterGridPane = new GridPane();
			filterGridPane.setPadding(new Insets(5, 5, 5, 5));
			
			filterGridPane.setHgap(8);
			filterGridPane.setVgap(4);
			
			filterGridPane.add(new Label("Search Text: "), 0, 0);
			filterGridPane.add(searchTextField, 1, 0);
			
			filterGridPane.add(new Label("Sort by: "), 0, 1);
			sortChoiceBox.getItems().addAll(SortCriterion.values());
			sortChoiceBox.setValue(SortCriterion.DEFAULT);
			sortChoiceBox.setPrefWidth(150);
			filterGridPane.add(sortChoiceBox, 1, 1);
			filterGridPane.add(filterButton, 2, 1);
			filterPane.setText("Filter");
			filterPane.setContent(filterGridPane);
			
			mainPane.setTop(filterPane);
			
			// middle area (the song table)
			songList = new SongTable(playList);
			mainPane.setCenter(songList);
			songList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //copied from the internet to fix sizing issue

			
			
			// bottom area 
			VBox bottomVBox = new VBox();
			bottomVBox.setPadding(new Insets(5, 5, 5, 5));
			GridPane songInfo = new GridPane();
			songInfo.add(new Label("Playlist: "), 0, 0);
			songInfo.add(playListLabel, 1, 0);
			songInfo.add(new Label("Current Song: "), 0, 1);
			songInfo.add(currentSongLabel, 1, 1);
			songInfo.add(new Label("Duration: "), 0, 2);
			songInfo.add(playTimeLabel, 1, 2);
			
			HBox buttonRow = new HBox();
			playButton = makeButton("play.jpg");
			pauseButton = makeButton("pause.jpg");
			stopButton = makeButton("stop.jpg");
			nextButton = makeButton("next.jpg");
			buttonRow.getChildren().addAll(playButton, pauseButton, stopButton, nextButton);
			buttonRow.setAlignment(Pos.CENTER);
			buttonRow.setSpacing(8);
			
			bottomVBox.getChildren().addAll(songInfo,buttonRow);
			mainPane.setBottom(bottomVBox);
			
			// Initial Button Layout
			
			//Event Handling
			
			// Sorting songs by the use of lambda expressions
			filterButton.setOnAction(e -> {
				playList.setSearch(searchTextField.getText());
				playList.setSortCriterion(sortChoiceBox.getValue());
				songList.refreshSongs();
				
			});
			
			// button actions
			
			playButton.setOnAction(e -> { 
				playCurrentSong(); 
				});
			
			pauseButton.setOnAction(e -> { 
				pauseCurrentSong(); 
				});
			
			stopButton.setOnAction(e -> { 
				stopCurrentSong();
				});
			
			nextButton.setOnAction(e -> {
				playNextSong();
			});
		
			songList.setRowSelectionHandler(e -> {
				playList.jumpToAudioFile(songList.getSelectionModel().getSelectedItem().getAudioFile());
				songList.selectSong(songList.getSelectionModel().getSelectedItem().getAudioFile());
				stopCurrentSong();
				updateSongInfo(songList.getSelectionModel().getSelectedItem().getAudioFile());
			});
		
		Scene scene = new Scene(mainPane, 600, 400);
		stage.setScene(scene);
		stage.show();
		
	}
        
		
	private void stopCurrentSong() {
		terminateThreads(false);
		AudioFile current = playList.currentAudioFile();
	    if (current != null) {
	        current.stop();
	    }
		playList.currentAudioFile().stop();
		setButtonStates(false, true, true, false);
	} 

	private boolean isPaused = true; //for paused method to stop and start the playtime 
	private void pauseCurrentSong() {
		if (isPaused) {
			isPaused = false;
			startThreads(true);
		}
		else {
			isPaused = true;
			terminateThreads(true);
		}	
		playList.currentAudioFile().togglePause();
		setButtonStates(true, false, false, false);
	}
	
	private void playNextSong() {
		terminateThreads(false);
		stopCurrentSong();
		playList.nextSong();
		setButtonStates(true, false, false, false);
		updateSongInfo(playList.currentAudioFile());
		playCurrentSong();
	}

	private void playCurrentSong() {
		startThreads(false);
		updateSongInfo(playList.currentAudioFile());
		setButtonStates(true, false, false, false);
	
	}
	
	private void setButtonStates(boolean playDisabled, boolean pauseDisabled, boolean stopDisabled, boolean nextDisabled) {
			playButton.setDisable(playDisabled);
			pauseButton.setDisable(pauseDisabled);
			stopButton.setDisable(stopDisabled);
			nextButton.setDisable(nextDisabled);
		}

	
	
	private void updateSongInfo(AudioFile af) { 
		Platform.runLater(() -> { 
			if (af == null) { 
				currentSongLabel.setText(NO_CURRENT_SONG) ;
				playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
			} 
			else { 
				currentSongLabel.setText(af.getTitle());
				playTimeLabel.setText(af.formatPosition());
			} 
		}); 
	} 
	

	public void setUseCertPlayList(boolean value) {
		this.useCertPlayList = value;
	}
	
	private void setPlayList(String pathname) { //cert was failing 
			playList = new PlayList(pathname);
			songList.refreshSongs();
		
	}
	
	
	private Button makeButton(String iconfile) { 
	    Button button = null; 
	    try { 
	        URL url = getClass().getResource("/icons/" + iconfile); 
	        Image icon = new Image(url.toString()); 
	        ImageView imageView = new ImageView(icon); 
	        imageView.setFitHeight(20);  
	        imageView.setFitWidth(20);  
	        button = new Button("", imageView); 
	        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY); 
	        button.setStyle("-fx-background-color: #fff;"); 
	    } catch (Exception e) { 
	        System.out.println("Image " + "icons/"  
	            + iconfile + " not found!"); 
	        System.exit(-1); 
	    } 
	    return button; 
	} 
	
	// Threads
	class PlayerThread extends Thread{
		private boolean stopped  = false;
		@Override
		public void run() {
			while (!stopped) {
				if(playList.currentAudioFile() != null) {
				try {
					playList.currentAudioFile().play();
				} catch (NotPlayableException e) {
					terminate();
				}	
				}
			}
		}
		
		public void terminate() {
				stopped = true;
		}
	}
	
	class TimerThread extends Thread {
		private boolean stopped = false;
		
		public void run() {
			while (!stopped) {
				if (playList.currentAudioFile() != null) {
					updateSongInfo(playList.currentAudioFile());
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
		
		public void terminate() {
			stopped = true;
		}
	}
	
	TimerThread timer = new TimerThread();
	PlayerThread player = new PlayerThread();

	
	private void startThreads(boolean onlyTimer) { 
		timer = new TimerThread();
		timer.start();
		if (!onlyTimer) {
			player = new PlayerThread();
			player.start();
		}
	} 
	
	private void terminateThreads(boolean onlyTimer) { 
		timer.terminate();
		
		if (!onlyTimer) {
			player.terminate();
		}
		}


	


	private boolean isUseCertPlayList() { //cert was failing
		return useCertPlayList;
	}



	
	public void loadPlayList (String pathname) {
		if (pathname == null || pathname.isEmpty()) {
			playlistPath = DEFAULT_PLAYLIST;
		}
		else {
			playlistPath = pathname;			
		}
		playListLabel.setText(playlistPath);
		 if (playList == null) {
		        playList = new PlayList(playlistPath);
		    }
		    else {
		        playList.loadFromM3U(playlistPath);
		    }

		    if (songList != null) {
		        songList.refreshSongs();
		    }
		    setButtonStates(false, true, true, false);
	}
	
	public void createButton(String iconfile) { //cert was failing due to name overlap
		
	}
	
	public static void main(String[] args) {
	    launch(args);
	}

// commented out since i was failing tests after a default setter/getter implementation
	
//	public String getPLAYLIST_DIRECTORY() {
//		return PLAYLIST_DIRECTORY;
//	}
//
//
//	public void setPLAYLIST_DIRECTORY(String pLAYLIST_DIRECTORY) {
//		PLAYLIST_DIRECTORY = pLAYLIST_DIRECTORY;
//	}
//
//
//	public String getINITIAL_PLAY_TIME_LABEL() {
//		return INITIAL_PLAY_TIME_LABEL;
//	}
//
//
//	public void setINITIAL_PLAY_TIME_LABEL(String iNITIAL_PLAY_TIME_LABEL) {
//		INITIAL_PLAY_TIME_LABEL = iNITIAL_PLAY_TIME_LABEL;
//	}
//
//
//	public String getNO_CURRENT_SONG() {
//		return NO_CURRENT_SONG;
//	}
//
//
//	public void setNO_CURRENT_SONG(String nO_CURRENT_SONG) {
//		NO_CURRENT_SONG = nO_CURRENT_SONG;
//	}
//	
//	public Button     getPlayButton(){
//		return playButton; }
//	
//	public Button     getPauseButton(){
//		return pauseButton;
//	}
//	
//	public Button getStopButton() {
//	    return stopButton;
//	}
//
//	public Button getNextButton() {
//	    return nextButton;
//	}
//
//	public Label getPlayListLabel() {
//	    return playListLabel;
//	}
//
//	public Label getPlayTimeLabel() {
//	    return playTimeLabel;
//	}
//
//	public Label getCurrentSongLabel() {
//	    return currentSongLabel;
//	}
//
//	public ChoiceBox<SortCriterion> getSortChoiceBox() {
//	    return sortChoiceBox;
//	}
//
//	public TextField getSearchTextField() {
//	    return searchTextField;
//	}
//
//	public Button getFilterButton() {
//	    return filterButton;
//	}

}
