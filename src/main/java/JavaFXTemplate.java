import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXTemplate extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/welcomeScreen.fxml"));
            Scene scene = new Scene(root, 1500, 750);
            // Default theme
            scene.getStylesheets().add(getClass().getResource("/styles/darkTheme.css").toExternalForm());

            primaryStage.setTitle("Three Card Poker Client - Welcome");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
