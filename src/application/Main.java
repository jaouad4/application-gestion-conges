package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("presentation/vacation_request.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("Gestion des Congés");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}