import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection {

    private String ip;
    private int port;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private gameplayController gc;

    public ClientConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean connect() {
        try {
            socket = new Socket(ip, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            Thread listener = new Thread(this::listenFromServer);
            listener.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setGameplayController(gameplayController gc) {
        this.gc = gc;
    }

    public void sendToServer(PokerInfo info) {
        try {
            output.writeObject(info);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
            if (gc != null) gc.updateGameInfo("Error sending to server: " + e.getMessage());
        }
    }

    private void listenFromServer() {
        try {
            while (socket != null && !socket.isClosed()) {
                PokerInfo info = (PokerInfo) input.readObject();
                handleServerResponse(info);
            }
        } catch (Exception e) {
            if (gc != null) gc.updateGameInfo("Connection lost.");
        }
    }

    private void handleServerResponse(PokerInfo info) {
        switch (info.getAction()) {
            case "BETS_ACCEPTED":
                gc.betsAccepted();
                break;
            case "CARDS_DEALT":
                gc.displayCards(info.getPlayerHand(), info.getDealerHand(), true);
                break;
            case "SHOW_DEALER":
                gc.displayCards(info.getPlayerHand(), info.getDealerHand(), false);
                break;
            case "ROUND_RESULT":
                gc.showResult(info.getResultMessage(), info.getTotalWinnings());
                break;
            default:
                gc.updateGameInfo("Unknown action from server: " + info.getAction());
                break;
        }
    }
}
