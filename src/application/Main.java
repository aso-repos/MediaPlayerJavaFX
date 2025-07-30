package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start (Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mp3player.fxml"));
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/css/mp3style.css").toExternalForm());

        Image icon = new Image(getClass().getResourceAsStream("/icons/aso_logo.png"));
        stage.getIcons().add(icon);

        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }


    public static void main (String[] args) {

        launch(args);
    }
}
