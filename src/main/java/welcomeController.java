import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class welcomeController {

    @FXML private TextField ipField;
    @FXML private TextField portField;
    @FXML private Button connectButton;

    private ClientConnection clientConnection;

    @FXML
    private void handleConnect(ActionEvent event) {
        try {
            String ip = ipField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());

            clientConnection = new ClientConnection(ip, port);
            boolean connected = clientConnection.connect();
            if (!connected) {
                showAlert("Connection Failed", "Could not connect to server. Check IP and Port.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameplayScreen.fxml"));
            Parent root = loader.load();

            gameplayController controller = loader.getController();
            controller.setClientConnection(clientConnection);

            Scene scene = new Scene(root, 1500, 750);
            scene.getStylesheets().add(getClass().getResource("/styles/darkTheme.css").toExternalForm());

            Stage stage = (Stage) connectButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Three Card Poker Client - Gameplay");
            stage.show();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid port number.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
