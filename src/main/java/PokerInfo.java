import java.io.Serializable;
import java.util.ArrayList;

public class PokerInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String action;
    private int anteBet;
    private int pairPlusBet;
    private boolean play;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;
    private int totalWinnings;
    private String resultMessage;

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public int getAnteBet() { return anteBet; }
    public void setAnteBet(int anteBet) { this.anteBet = anteBet; }

    public int getPairPlusBet() { return pairPlusBet; }
    public void setPairPlusBet(int pairPlusBet) { this.pairPlusBet = pairPlusBet; }

    public boolean isPlay() { return play; }
    public void setPlay(boolean play) { this.play = play; }

    public ArrayList<Card> getPlayerHand() { return playerHand; }
    public void setPlayerHand(ArrayList<Card> playerHand) { this.playerHand = playerHand; }

    public ArrayList<Card> getDealerHand() { return dealerHand; }
    public void setDealerHand(ArrayList<Card> dealerHand) { this.dealerHand = dealerHand; }

    public int getTotalWinnings() { return totalWinnings; }
    public void setTotalWinnings(int totalWinnings) { this.totalWinnings = totalWinnings; }

    public String getResultMessage() { return resultMessage; }
    public void setResultMessage(String resultMessage) { this.resultMessage = resultMessage; }
}
