import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class resultController {

    @FXML private Label resultLabel;
    @FXML private Label winningsLabel;

    private ClientConnection clientConnection;
    private String resultMessage;
    private int totalWinnings;

    public void setResults(String resultMessage, int totalWinnings, ClientConnection connection) {
        this.resultMessage = resultMessage;
        this.totalWinnings = totalWinnings;
        this.clientConnection = connection;
        resultLabel.setText(resultMessage);
        winningsLabel.setText("$" + totalWinnings);
    }

    @FXML
    private void handlePlayAgain(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/gameplayScreen.fxml"));
            Parent root = loader.load();
            gameplayController gc = loader.getController();
            gc.setClientConnection(clientConnection);
            gc.updateGameInfo("New round started. Place your bets.");

            Scene scene = resultLabel.getScene();
            Stage stage = (Stage) scene.getWindow();
            scene.setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        javafx.application.Platform.exit();
    }
}
