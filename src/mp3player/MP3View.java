package mp3player;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;

import java.net.URL;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Font.loadFont(getClass().getResourceAsStream("/MediaPlayer.ttf"), 12);


    }

    @FXML
    public void playpauseClicked (ActionEvent event) {

        System.out.println("Play/Pause Clicked");
    }

    @FXML
    public void stopClicked (ActionEvent event) {

        System.out.println("Stop Clicked");
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