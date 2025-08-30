package mp3player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MP3View implements Initializable {

    @FXML
    private Button playpauseButton;
    @FXML
    private Button skipbackButton;
    @FXML
    private Button skipforwardButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button skipbeginningButton;
    @FXML
    private Button skipendingButton;
    @FXML
    private Button muteButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button sourceButton;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label songTitle;
    @FXML
    private ListView playlistView;
    @FXML
    Label timerLabel;
    @FXML
    Rectangle eqBar0;
    @FXML
    Rectangle eqBar1;
    @FXML
    Rectangle eqBar2;
    @FXML
    Rectangle eqBar3;
    @FXML
    Rectangle eqBar4;
    @FXML
    Rectangle eqBar5;
    @FXML
    Rectangle eqBar6;
    @FXML
    Rectangle eqBar7;
    @FXML
    Rectangle eqBar8;
    @FXML
    Rectangle eqBar9;
    @FXML
    Rectangle eqBar10;
    @FXML
    Rectangle eqBar11;
    @FXML
    Rectangle eqBar12;
    @FXML
    Rectangle eqBar13;
    @FXML
    Rectangle eqBar14;
    @FXML
    Rectangle eqBar15;

    private int currentTrackIndex = 0;
    private List<String> trackList = new ArrayList<>();
    private Media mpthreeMedia;
    private MediaPlayer mpthreePlayer;

    // Create ArrayList for all EQ bars
    private List<Rectangle> eqBars;

    // File/Playlist Menu field declaration for "Source" button
    private ContextMenu sourceMenu;
    private MenuItem instantPlayItem;
    private MenuItem instantBatchItem;
    private MenuItem newPlaylistItem;
    private MenuItem loadPlaylistItem;
    private MenuItem savePlaylistItem;
    private MenuItem deletePlaylistItem;
    private MenuItem appendFilesItem;
    private MenuItem replaceFilesItem;

    // Fields for instant batch
    private List<String> instantBatchUri;
    private List<File> instantBatchFiles;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Font.loadFont(getClass().getResourceAsStream("/fonts/MediaPlayer.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/MatrixCustom.ttf"), 12);

        String[] testFiles = {"/music/Blue_Giraffe_-_Epic_Trailer_Inspire.mp3",
                "/music/Metal Impact Sound Effects - Sample Library.mp3",
                "/music/Morning Guided Meditation For Positive Energy & Motivation.mp3"};

        // Add test files trackList ArrayList
        for (String path : testFiles) {
            URL trackUrl = getClass().getResource(path);
            if (trackUrl != null) {
                trackList.add(trackUrl.toExternalForm());
            } else {
                System.out.println("Track Not Found: " + path);
            }
        }

        if (!trackList.isEmpty()) {
            int counter = 0;
            for (String track : trackList) {

                System.out.println("Track " + counter + " Loaded: " + trackList.get(counter));
                counter++;
            }
        } else {

            System.out.println("No Tracks Loaded");
        }

        // Check that trackList not empty and load current track into MediaPlayer
        if (!trackList.isEmpty()) {
            loadTrack();
        }

        // Add EQ bars to ArrayList
        eqBars = new ArrayList<>(Arrays.asList(eqBar0, eqBar1, eqBar2, eqBar3,
                        eqBar4, eqBar5, eqBar6, eqBar7,
                        eqBar8, eqBar9, eqBar10, eqBar11,
                        eqBar12, eqBar13, eqBar14, eqBar15));

        // Create the menu items for File/Playlist from "Source" button
        instantPlayItem = new MenuItem("Instant Play (Single File)");
        instantBatchItem = new MenuItem("Instant Batch (Multiple Files)");
        newPlaylistItem = new MenuItem("New Playlist");
        loadPlaylistItem = new MenuItem("Load Playlist");
        savePlaylistItem = new MenuItem("Save Playlist");
        deletePlaylistItem = new MenuItem("Delete Playlist");
        appendFilesItem = new MenuItem("Append Files");
        replaceFilesItem = new MenuItem("Replace Files");

        // Create context menu for File/Playlist from "Source" button
        sourceMenu = new ContextMenu(
                instantPlayItem, instantBatchItem,
                new SeparatorMenuItem(),
                newPlaylistItem, loadPlaylistItem, savePlaylistItem, deletePlaylistItem,
                new SeparatorMenuItem(),
                appendFilesItem, replaceFilesItem
        );

        // Connect the menu items to their implementation methods
        instantPlayItem.setOnAction(e -> onInstantPlaySingle());
        instantBatchItem.setOnAction(e -> onInstantPlayBatch());
        newPlaylistItem.setOnAction(e -> onNewPlaylist());
        loadPlaylistItem.setOnAction(e -> onLoadPlaylist());
        savePlaylistItem.setOnAction(e -> onSavePlaylist());
        deletePlaylistItem.setOnAction(e -> onDeletePlaylist());
        appendFilesItem.setOnAction(e -> onAppendFiles());
        replaceFilesItem.setOnAction(e -> onReplaceFiles());

        // Create listener for playListView
        playlistView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0) {
                currentTrackIndex = newVal.intValue();
                loadTrack();
                mpthreePlayer.play();
            }

        });
    }

    // Implement play/pause button
    @FXML
    public void playpauseClicked(ActionEvent event) {

        if (mpthreePlayer != null) {
            MediaPlayer.Status status = mpthreePlayer.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                mpthreePlayer.pause();
                System.out.println("PAUSED");
            } else {
                mpthreePlayer.play();
                System.out.println("PLAYING");
            }
        }
    }

    // Implement stop button
    @FXML
    public void stopClicked(ActionEvent event) {

        if (mpthreePlayer != null) {
            mpthreePlayer.stop();
        }
        System.out.println("Stop Clicked");
    }

    // Implement skip-to-previous track button
    @FXML
    public void skipbackClicked(ActionEvent event) {

        System.out.println("Current Track: " + trackList.get(currentTrackIndex));
        System.out.println("Skip To Previous Track Clicked");

        if (!trackList.isEmpty()) {
            mpthreePlayer.stop();
            if (currentTrackIndex > 0) {
                currentTrackIndex--;
            } else {
                currentTrackIndex = trackList.size() - 1;
            }
            loadTrack();
        }
    }

    // Implement skip-to-beginning button
    @FXML
    public void skipbeginningClicked(ActionEvent event) {

        System.out.println("Skip To Beginning");

        if (mpthreePlayer != null) {
            mpthreePlayer.seek(Duration.ZERO);
        }
    }

    // Implement skip-to-ending button
    @FXML
    public void skipendingClicked(ActionEvent event) {

        System.out.println("Skip To End Clicked");

        if (mpthreePlayer != null) {
            Duration endTime = mpthreePlayer.getTotalDuration();
            if (endTime != null && endTime.greaterThan(Duration.ZERO)) {
                mpthreePlayer.seek(endTime);
            }
        }
    }

    // Implement skip-to-next track button
    @FXML
    public void skipforwardClicked(ActionEvent event) {

        System.out.println("Current Track: " + trackList.get(currentTrackIndex));
        System.out.println("Skip To Next Track Clicked");

        if (!trackList.isEmpty() && currentTrackIndex < trackList.size() - 1) {

            mpthreePlayer.stop();
            currentTrackIndex++;
            loadTrack();

        } else if (!trackList.isEmpty() && currentTrackIndex >= trackList.size() - 1) {
            mpthreePlayer.stop();
            currentTrackIndex = 0;
            loadTrack();
        }
    }

    // Implement volume control slider
    @FXML
    public void volumeControl(MouseEvent event) {

        if (mpthreePlayer != null) {
            double volume = volumeSlider.getValue() / 100.0;
            mpthreePlayer.setVolume(volume);
        }
    }

    // Implement mute button
    @FXML
    public void muteClicked(ActionEvent event) {

        if (mpthreePlayer != null) {
            if (!mpthreePlayer.isMute()) {
                mpthreePlayer.setMute(true);
            } else {
                mpthreePlayer.setMute(false);
            }
        }
    }

    @FXML
    public void sourceClicked (ActionEvent event) {
        sourceMenu.show(sourceButton, Side.BOTTOM, 0,0);
    }

    @FXML
    public void closeClicked (ActionEvent event) {

        if (mpthreePlayer != null) {
            mpthreePlayer.stop();
            mpthreePlayer.dispose();
        }
        javafx.application.Platform.exit();
    }

    /* Set up methods for implementing menu items in the File/Playlist menu
    triggered by "Source" button */

    // Instantly play track without affecting trackList
    public void onInstantPlaySingle () {

        // Choose a file first
        FileChooser fileChooser = new FileChooser();

        // Add MP3 extension filter
        FileChooser.ExtensionFilter mp3Filter =
                new FileChooser.ExtensionFilter("MP3 Files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(mp3Filter);

        // Choose single file
        File selectedFile = fileChooser.showOpenDialog(sourceButton.getScene().getWindow());

        if (selectedFile != null) {
            String uri = selectedFile.toURI().toString();

            // Remove old player if it's active
            if (mpthreePlayer != null) {
                mpthreePlayer.stop();
                mpthreePlayer.dispose();
            }

            List<String> tempList = new ArrayList<>();
            tempList.add(uri);

            loadTrackBatch(tempList, 0);

        } else {
            System.out.println("No Files Selected");
        }

        System.out.println("Instant Play");
    }

    // Instantly play multiple tracks without affecting trackList
    public void onInstantPlayBatch () {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter mp3Filter =
                new FileChooser.ExtensionFilter("MP3 Files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(mp3Filter);

        // Pick multiple files
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(sourceButton.getScene().getWindow());

        // Remove old player if it's active
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            if (mpthreePlayer != null) {
                mpthreePlayer.stop();
                mpthreePlayer.dispose();
            }


            // Build temporary list
            List<String> tempList = new ArrayList<>();
            for (File file : selectedFiles) {
                tempList.add(file.toURI().toString());
            }

            loadTrackBatch(tempList, 0);

            System.out.println("Instant Batch Playing Started: " + selectedFiles.size());
        } else {
            System.out.println("No Selected Files");
        }
    }

    public void onNewPlaylist () {
        System.out.println("New Playlist");
    }

    public void onLoadPlaylist () {
        System.out.println("Load Playlist");
    }

    public void onSavePlaylist () {
        System.out.println("Save PlayList");
    }

    public void onDeletePlaylist () {
        System.out.println("Delete Playlist");
    }

    public void onAppendFiles () {

        // Choose a file first
        FileChooser fileChooser = new FileChooser();

        // Add MP3 extension filter
        FileChooser.ExtensionFilter mp3Filter =
                new FileChooser.ExtensionFilter("MP3 Files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(mp3Filter);

        // Allow multiple files to be selected
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(sourceButton.getScene().getWindow());

        // See if any files were selected
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            for (File file: selectedFiles) {
                String uri = file.toURI().toString();
                trackList.add(uri);
                playlistView.getItems().add(file.getName());
                System.out.println("Files Added To Track List: " + file.getName());
            }

            // Set index to first newly selected file
            if (mpthreePlayer == null && !trackList.isEmpty()) {
                currentTrackIndex = trackList.size() - selectedFiles.size();
                loadTrack();
            }
        } else {
            System.out.println("No Files Selected");
        }

        System.out.println("Append Files");
    }

    public void onReplaceFiles () {

        // Choose a file first
        FileChooser fileChooser = new FileChooser();

        // Add MP3 extension filter
        FileChooser.ExtensionFilter mp3Filter =
                new FileChooser.ExtensionFilter("MP3 Files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(mp3Filter);

        // Allow multiple files to be selected
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(sourceButton.getScene().getWindow());

        // See if any files were selected
        if (selectedFiles != null && !selectedFiles.isEmpty()) {

            trackList.clear();
            playlistView.getItems().clear();

            for (File file: selectedFiles) {
                String uri = file.toURI().toString();
                trackList.add(uri);
                playlistView.getItems().add(file.getName());
                System.out.println("Files Added To Track List: " + file.getName());
            }
        } else {
            System.out.println("No Files Selected");
        }

        currentTrackIndex = 0;
        loadTrack();
        System.out.println("Replace Files");
    }

    /* Helper method to set the state of the player under different conditions
    - Set up display for track
    - Set up progress bar
     */
    public void loadTrack() {

        boolean isPlaying = (mpthreePlayer != null && mpthreePlayer.getStatus() == MediaPlayer.Status.PLAYING);
        boolean isMuted = (mpthreePlayer != null && mpthreePlayer.isMute());

        double getVolume;
        if (mpthreePlayer != null) {
            getVolume = mpthreePlayer.getVolume();
            mpthreePlayer.stop();
            mpthreePlayer.dispose();
        } else {
            getVolume = volumeSlider.getValue() / 100;
        }

        mpthreeMedia = new Media(trackList.get(currentTrackIndex));
        mpthreePlayer = new MediaPlayer(mpthreeMedia);

        // Set initial elapsed/total time for timerLabel
        mpthreePlayer.setOnReady(() -> {
            Object realTitle = mpthreeMedia.getMetadata().get("title");
            timerLabel.setText(timeFormat(Duration.ZERO) +"/" + timeFormat(mpthreePlayer.getTotalDuration()));
        });

        // Set up number of EQ bands for AudioSpectrumListener
        mpthreePlayer.setAudioSpectrumNumBands(16);
        mpthreePlayer.setAudioSpectrumInterval(0.05);
        mpthreePlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phase) -> {
            for (int i = 0; i < eqBars.size(); i++) {
                Rectangle bar = eqBars.get(i);
                double height = magnitudes[i] + 60;
                if (height < 2) {
                    height = 2;
                }
                double scaledHeight = height * 1.5;
                double baseline = bar.getY() + bar.getHeight();
                eqBars.get(i).setHeight(scaledHeight);
                eqBars.get(i).setY(baseline - scaledHeight);
            }
        });

        mpthreePlayer.setVolume(getVolume);
        volumeSlider.setValue(getVolume * 100);

        // Listens for track-end and go to/play next track (lambda expression)
        mpthreePlayer.setOnEndOfMedia(() -> {
            currentTrackIndex++;
            if (currentTrackIndex >= trackList.size()) {
                currentTrackIndex = 0;
            }
            loadTrack();
            mpthreePlayer.play();
        });

        // The long "beginner friendly" version of above task
//        mpthreePlayer.setOnEndOfMedia(new Runnable() {
//            @Override
//            public void run() {
//                currentTrackIndex++;
//                if (currentTrackIndex >= trackList.size()) {
//                    currentTrackIndex = 0;
//                }
//                loadTrack();
//                mpthreePlayer.play();
//            }
//        });

        // Set the song title to current track
        String fullPath = trackList.get(currentTrackIndex);
        try {
            URL url = new URL(fullPath);
            String path = url.getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            songTitle.setText(fileName);
            playlistView.getSelectionModel().select(currentTrackIndex);
            playlistView.scrollTo(currentTrackIndex);
        } catch (Exception e) {
            songTitle.setText("Unknown Track");
        }

        // Set up progress bar and add listener for track time
        mpthreePlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            Duration total = mpthreePlayer.getTotalDuration();
            if (total != null && total.toMillis() > 0) {
                double progress = newTime.toMillis() / total.toMillis();
                progressBar.setProgress(progress);

                // Set up elapsed/total time for timerLabel
                timerLabel.setText(timeFormat(newTime) + "/" + timeFormat(total));

            } else {
                progressBar.setProgress(0);
            }
        });

        progressBar.setOnMousePressed(event -> seekFromProgress(event));
        progressBar.setOnMouseDragged(event -> seekFromProgress(event));

        System.out.println("Current Track: " + trackList.get(currentTrackIndex));

        if (isPlaying) {
            mpthreePlayer.play();
        }

        if (isMuted) {
            mpthreePlayer.setMute(true);
        }
    }

    // Modified loadTrack method for Instant Batch Playing
    public void loadTrackBatch(List<String> tempList, int tempIndex) {

        boolean isPlaying = (mpthreePlayer != null && mpthreePlayer.getStatus() == MediaPlayer.Status.PLAYING);
        boolean isMuted = (mpthreePlayer != null && mpthreePlayer.isMute());

        double getVolume;
        if (mpthreePlayer != null) {
            getVolume = mpthreePlayer.getVolume();
            mpthreePlayer.stop();
            mpthreePlayer.dispose();
        } else {
            getVolume = volumeSlider.getValue() / 100;
        }

        mpthreeMedia = new Media(tempList.get(tempIndex));
        mpthreePlayer = new MediaPlayer(mpthreeMedia);

        // Set initial elapsed/total time for timerLabel
        mpthreePlayer.setOnReady(() -> {
            Object realTitle = mpthreeMedia.getMetadata().get("title");
            timerLabel.setText(timeFormat(Duration.ZERO) +"/" + timeFormat(mpthreePlayer.getTotalDuration()));
        });

        // Set up number of EQ bands for AudioSpectrumListener
        mpthreePlayer.setAudioSpectrumNumBands(16);
        mpthreePlayer.setAudioSpectrumInterval(0.05);
        mpthreePlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phase) -> {
            for (int i = 0; i < eqBars.size(); i++) {
                Rectangle bar = eqBars.get(i);
                double height = magnitudes[i] + 60;
                if (height < 2) {
                    height = 2;
                }
                double scaledHeight = height * 1.5;
                double baseline = bar.getY() + bar.getHeight();
                eqBars.get(i).setHeight(scaledHeight);
                eqBars.get(i).setY(baseline - scaledHeight);
            }
        });

        mpthreePlayer.setVolume(getVolume);
        volumeSlider.setValue(getVolume * 100);

        // Listens for track-end and go to/play next track (lambda expression)
        final int[] indexWrapper = {tempIndex};
        mpthreePlayer.setOnEndOfMedia(() -> {
            indexWrapper[0]++;
            if (indexWrapper[0] < tempList.size()) {
                loadTrackBatch(tempList, indexWrapper[0]);
                mpthreePlayer.play();
            } else {
                System.out.println("Instant Batch Finished Playing");
            }
        });

        // The long "beginner friendly" version of above task
//        mpthreePlayer.setOnEndOfMedia(new Runnable() {
//            @Override
//            public void run() {
//                tempIndex++;
//                if (tempIndex >= tempList.size()) {
//                    tempIndex = 0;
//                }
//                loadTrack();
//                mpthreePlayer.play();
//            }
//        });

        // Set the song title to current track
        String fullPath = tempList.get(tempIndex);
        try {
            URL url = new URL(fullPath);
            String path = url.getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            songTitle.setText(fileName);
        } catch (Exception e) {
            songTitle.setText("Unknown Track");
        }

        // Set up progress bar and add listener for track time
        mpthreePlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            Duration total = mpthreePlayer.getTotalDuration();
            if (total != null && total.toMillis() > 0) {
                double progress = newTime.toMillis() / total.toMillis();
                progressBar.setProgress(progress);

                // Set up elapsed/total time for timerLabel
                timerLabel.setText(timeFormat(newTime) + "/" + timeFormat(total));

            } else {
                progressBar.setProgress(0);
            }
        });

        progressBar.setOnMousePressed(event -> seekFromProgress(event));
        progressBar.setOnMouseDragged(event -> seekFromProgress(event));

        System.out.println("Current Track: " + tempList.get(tempIndex));

        if (isPlaying) {
            mpthreePlayer.play();
        }

        if (isMuted) {
            mpthreePlayer.setMute(true);
        }
        mpthreePlayer.play();
    }

    // Helper method to set up progress bar and handle progress bar logic
    public void seekFromProgress (MouseEvent event) {
        if (mpthreePlayer != null) {
            Duration total = mpthreePlayer.getTotalDuration();
            if (total != null && total.toMillis() > 0) {
                double mouseX = event.getX();
                double width = progressBar.getWidth();
                double fraction = mouseX / width;
                if (fraction < 0) fraction = 0;
                if (fraction > 1) fraction = 1;
                Duration seekTime = total.multiply(fraction);
                mpthreePlayer.seek(seekTime);
            }
        }
    }
     // Helper method for displaying elapsed/total time
    public String timeFormat (Duration time) {

        int minutes = (int) time.toMinutes();
        int seconds = (int) (time.toSeconds() % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }
}