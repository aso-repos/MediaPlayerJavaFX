package mp3player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;

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

                System.out.println("Track " +counter + " Loaded: " + trackList.get(counter));
                counter ++;
                }
        } else {

            System.out.println("No Tracks Loaded");
        }

        //Check that trackList not empty and load current track into MediaPlayer
        if (!trackList.isEmpty()) {
            mpthreeMedia = new Media(trackList.get(currentTrackIndex));
            mpthreePlayer = new MediaPlayer(mpthreeMedia);
            System.out.println("MP3 PLayer Ready For Track: " + trackList.get(currentTrackIndex));

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
    public void playpauseClicked (ActionEvent event) {

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
    public void stopClicked (ActionEvent event) {

        mpthreePlayer.stop();
        System.out.println("Stop Clicked");

        stopButton.setDisable(true);
        skipbeginningButton.setDisable(true);
        skipendingButton.setDisable(true);
    }

    @FXML
    public void skipbackClicked (ActionEvent event) {

        System.out.println("Skip To Previous Track Clicked");
    }

    @FXML
    public void skipbeginningClicked (ActionEvent event) {

        System.out.println("Skip To Beginning");
    }

    @FXML
    public void skipendingClicked (ActionEvent event) {

        System.out.println("Skip To End Clicked");
    }

    @FXML
    public void skipforwardClicked (ActionEvent event) {

        System.out.println("Skip To Next Track Clicked");
    }

    @FXML
    public void volumeControl (ActionEvent event) {

    }

    @FXML
    public void muteClicked (ActionEvent event) {

    }



}