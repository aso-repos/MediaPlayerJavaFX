package mp3player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
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
    private ProgressBar progressBar;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label songTitle;

    private int currentTrackIndex = 0;
    private List<String> trackList = new ArrayList<>();
    private Media mpthreeMedia;
    private MediaPlayer mpthreePlayer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Font.loadFont(getClass().getResourceAsStream("/MediaPlayer.ttf"), 12);

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

        //Check that trackList not empty and load current track into MediaPlayer
        if (!trackList.isEmpty()) {
            loadTrack();

            // Enable player buttons
            playpauseButton.setDisable(false);
            progressBar.setDisable(false);
            volumeSlider.setDisable(false);
            muteButton.setDisable(false);

            // Enable skipping only if multiple tracks are available
            if (trackList.size() > 1) {
                skipbackButton.setDisable(false);
                skipforwardButton.setDisable(false);
            }
        }
    }

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

                // Enable rest of buttons used during playing
                stopButton.setDisable(false);
                skipbeginningButton.setDisable(false);
                skipendingButton.setDisable(false);
            }
        }
    }

    @FXML
    public void stopClicked(ActionEvent event) {

        mpthreePlayer.stop();
        System.out.println("Stop Clicked");

        stopButton.setDisable(true);
        skipbeginningButton.setDisable(true);
        skipendingButton.setDisable(true);
    }

    @FXML
    public void skipbackClicked(ActionEvent event) {

        System.out.println("Current Track Index: " + trackList.get(currentTrackIndex));
        System.out.println("Skip To Previous Track Clicked");

        if (!trackList.isEmpty() && currentTrackIndex < trackList.size() - 1 && currentTrackIndex > 0 || currentTrackIndex == trackList.size() - 1) {

            mpthreePlayer.stop();
            currentTrackIndex--;
            loadTrack();

        } else if (!trackList.isEmpty() && currentTrackIndex == 0) {
            mpthreePlayer.stop();
            currentTrackIndex = 0;
            loadTrack();
        }
    }

    @FXML
    public void skipbeginningClicked(ActionEvent event) {

        System.out.println("Skip To Beginning");

        mpthreePlayer.seek(Duration.ZERO);
    }

    @FXML
    public void skipendingClicked(ActionEvent event) {

        System.out.println("Skip To End Clicked");

        Duration endTime = mpthreePlayer.getTotalDuration();
        mpthreePlayer.seek(endTime);


    }

    @FXML
    public void skipforwardClicked(ActionEvent event) {

        System.out.println("Current Track Index: " + trackList.get(currentTrackIndex));
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

    @FXML
    public void volumeControl(MouseEvent event) {

        if (mpthreePlayer != null) {
            double volume = volumeSlider.getValue() / 100.0;
            mpthreePlayer.setVolume(volume);
        }


    }

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

    public void loadTrack() {

        boolean isPLaying = (mpthreePlayer != null && mpthreePlayer.getStatus() == MediaPlayer.Status.PLAYING);
        boolean isMuted = (mpthreePlayer != null && mpthreePlayer.isMute());
        double getVolume = 1.00;

        if (mpthreePlayer != null) {
            getVolume = mpthreePlayer.getVolume();
            mpthreePlayer.stop();
            mpthreePlayer.dispose();
        }

        mpthreeMedia = new Media(trackList.get(currentTrackIndex));
        mpthreePlayer = new MediaPlayer(mpthreeMedia);

        // Set the song title to current track
        String fullPath = trackList.get(currentTrackIndex);
        try {
            URL url = new URL(fullPath);
            String path = url.getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            songTitle.setText(fileName);
        } catch (Exception e) {
            songTitle.setText("Unknown Track");
        }

        mpthreePlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            Duration total = mpthreePlayer.getTotalDuration();
            if (total != null && total.toMillis() > 0) {
                double progress = newTime.toMillis() / total.toMillis();
                progressBar.setProgress(progress);
            } else {
                progressBar.setProgress(0);
            }
        });

        progressBar.setOnMousePressed(event -> seekFromProgress(event));

        System.out.println("Current Track Loaded: " + trackList.get(currentTrackIndex));

        if (isPLaying) {
            mpthreePlayer.setVolume(getVolume);
            mpthreePlayer.play();
        }

        if (isMuted) {
            mpthreePlayer.setMute(true);
        }
    }

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
}
