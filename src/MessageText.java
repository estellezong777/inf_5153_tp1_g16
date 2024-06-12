import java.util.concurrent.atomic.AtomicInteger;
public class MessageText {
    private static final AtomicInteger idMessage = new AtomicInteger(0); // id de message généré
    private final msgType type;
    private  String text;
    private final int numMessage;
    private final String Sender;
    private final String receiver;

    // La valeur 'messageIsActive' est un état de message.
    // Si l'état du message est active, il peut être transféré normalement. Sinon, indiquant que le message est perdu
    // par accident.
    private boolean messageIsActive = true;


    public MessageText( String sender, String receiver,msgType type, String text) {
        this.numMessage = idMessage.incrementAndGet();
        this.type = type;
        this.text = text;
        this.Sender = sender;
        this.receiver = receiver;

    }

    public int getNumMessage() {
        return numMessage;
    }

    public msgType getType() {
        return type;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return Sender;
    }

    public boolean isMessageActive() {
        return messageIsActive;
    }

    public void setMessageState(boolean messageState) {
        this.messageIsActive = messageState;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
