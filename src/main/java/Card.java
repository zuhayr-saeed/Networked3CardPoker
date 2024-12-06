import java.io.Serializable;

public class Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private int value; // 2-14 (11=J,12=Q,13=K,14=A)
    private char suit; // 'H','D','S','C'

    public Card(int value, char suit) {
        this.value = value;
        this.suit = suit;
    }

    public int getValue() { return value; }
    public char getSuit() { return suit; }

    @Override
    public String toString() {
        String valStr;
        switch(value) {
            case 11: valStr="J"; break;
            case 12: valStr="Q"; break;
            case 13: valStr="K"; break;
            case 14: valStr="A"; break;
            default: valStr=String.valueOf(value);
        }
        return valStr + suit;
    }
}
