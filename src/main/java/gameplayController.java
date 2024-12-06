import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;

public class gameplayController {

    @FXML private MenuBar menuBar;
    @FXML private HBox dealerHandPane;
    @FXML private HBox playerHandPane;
    @FXML private TextField anteField;
    @FXML private TextField pairPlusField;
    @FXML private Button placeBetsButton;
    @FXML private Button dealCardsButton;
    @FXML private Button playFoldButton;
    @FXML private TextArea gameInfoArea;
    @FXML private Label winningsLabel;

    private ClientConnection clientConnection;
    private int totalWinnings = 0;
    private int anteBet = 0;
    private int pairPlusBet = 0;
    private boolean waitingForPlayDecision = false;
    private String[] themes = {"/styles/darkTheme.css","/styles/lightTheme.css","/styles/neonTheme.css"};
    private int currentThemeIndex = 0;

    public void setClientConnection(ClientConnection connection) {
        this.clientConnection = connection;
        connection.setGameplayController(this);
    }

    public void updateGameInfo(String message) {
        Platform.runLater(() -> gameInfoArea.appendText(message + "\n"));
    }

    @FXML
    private void handlePlaceBets(ActionEvent event) {
        try {
            anteBet = Integer.parseInt(anteField.getText().trim());
            pairPlusBet = Integer.parseInt(pairPlusField.getText().trim());

            if (anteBet < 5 || anteBet > 25) {
                showAlert("Invalid Bet","Ante must be between $5 and $25");
                return;
            }
            if (pairPlusBet < 0 || pairPlusBet > 25) {
                showAlert("Invalid Bet","Pair Plus must be between $0 and $25");
                return;
            }

            PokerInfo info = new PokerInfo();
            info.setAction("PLACE_BETS");
            info.setAnteBet(anteBet);
            info.setPairPlusBet(pairPlusBet);
            clientConnection.sendToServer(info);

            updateGameInfo("Bets placed: Ante $" + anteBet + ", Pair Plus $" + pairPlusBet);
            placeBetsButton.setDisable(true);

        } catch (NumberFormatException e) {
            showAlert("Invalid Input","Please enter valid numeric values for bets.");
        }
    }

    public void betsAccepted() {
        Platform.runLater(() -> dealCardsButton.setDisable(false));
    }

    @FXML
    private void handleDealCards(ActionEvent event) {
        PokerInfo info = new PokerInfo();
        info.setAction("REQUEST_CARDS");
        clientConnection.sendToServer(info);
        dealCardsButton.setDisable(true);
    }

    public void displayCards(ArrayList<Card> playerHand, ArrayList<Card> dealerHand, boolean dealerHidden) {
        Platform.runLater(() -> {
            playerHandPane.getChildren().clear();
            dealerHandPane.getChildren().clear();

            for (Card c : playerHand) {
                playerHandPane.getChildren().add(createCardImageView(c));
            }

            for (Card c : dealerHand) {
                if (dealerHidden) {
                    dealerHandPane.getChildren().add(createCardBackImageView());
                } else {
                    dealerHandPane.getChildren().add(createCardImageView(c));
                }
            }
            updateGameInfo("Cards dealt. Choose to Play or Fold.");
            playFoldButton.setDisable(false);
            waitingForPlayDecision = true;
        });
    }

    @FXML
    private void handlePlayOrFold(ActionEvent event) {
        if (!waitingForPlayDecision) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Decision");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to PLAY or FOLD?");

        ButtonType playBtn = new ButtonType("Play");
        ButtonType foldBtn = new ButtonType("Fold", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(playBtn, foldBtn);

        boolean play = alert.showAndWait().orElse(foldBtn) == playBtn;

        PokerInfo info = new PokerInfo();
        info.setAction("PLAY_DECISION");
        info.setPlay(play);
        clientConnection.sendToServer(info);

        playFoldButton.setDisable(true);
        waitingForPlayDecision = false;
    }

    public void showResult(String resultMessage, int totalWinnings) {
        this.totalWinnings = totalWinnings;
        Platform.runLater(() -> {
            winningsLabel.setText("$" + totalWinnings);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/resultScreen.fxml"));
                Parent root = loader.load();
                resultController rc = loader.getController();
                rc.setResults(resultMessage, totalWinnings, clientConnection);

                Scene scene = menuBar.getScene();
                Stage stage = (Stage) scene.getWindow();
                scene.setRoot(root);

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error","Failed to load result screen: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleFreshStart(ActionEvent event) {
        totalWinnings = 0;
        winningsLabel.setText("$0");
        gameInfoArea.clear();
        anteField.clear();
        pairPlusField.clear();
        playerHandPane.getChildren().clear();
        dealerHandPane.getChildren().clear();
        placeBetsButton.setDisable(false);
        dealCardsButton.setDisable(true);
        playFoldButton.setDisable(true);
        updateGameInfo("Fresh start. Place your bets for a new hand.");
    }

    @FXML
    private void handleNewLook(ActionEvent event) {
        Scene scene = menuBar.getScene();
        scene.getStylesheets().clear();
        currentThemeIndex = (currentThemeIndex + 1) % themes.length;
        scene.getStylesheets().add(getClass().getResource(themes[currentThemeIndex]).toExternalForm());
        updateGameInfo("Theme changed to: " + themes[currentThemeIndex].substring(themes[currentThemeIndex].lastIndexOf('/')+1));
    }

    private ImageView createCardImageView(Card card) {
        ImageView iv = new ImageView();
        iv.setFitWidth(100);
        iv.setFitHeight(150);
        String imagePath = getCardFileName(card);
        iv.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        return iv;
    }

    private ImageView createCardBackImageView() {
        ImageView iv = new ImageView();
        iv.setFitWidth(100);
        iv.setFitHeight(150);
        iv.setImage(new Image(getClass().getResourceAsStream("/cards/back.png")));
        return iv;
    }

    private String getCardFileName(Card card) {
        String valueStr;
        switch (card.getValue()) {
            case 11: valueStr = "J"; break;
            case 12: valueStr = "Q"; break;
            case 13: valueStr = "K"; break;
            case 14: valueStr = "A"; break;
            default: valueStr = String.valueOf(card.getValue());
        }
        return "/cards/" + valueStr + card.getSuit() + ".png";
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
