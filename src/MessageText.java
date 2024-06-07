import java.util.concurrent.atomic.AtomicInteger;
public class MessageText {
    private static final AtomicInteger idMessage = new AtomicInteger(0);
    private final msgType type;
    private final String text;
    private final int numMessage;
    private final String Sender;
    private final String receiver;
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
}
