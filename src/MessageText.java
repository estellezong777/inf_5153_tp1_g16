import java.util.concurrent.atomic.AtomicInteger;
public class MessageText {
    private static final AtomicInteger idMessage = new AtomicInteger(0);
    private final msgType type;
    private final String text;
    private final int numMessage;
    private final String Sender;
    private final String receiver;
    private  boolean acknowledged;
    private boolean msgTreatmentState;


    public MessageText( String sender, String receiver,msgType type, String text) {
        this.numMessage = idMessage.incrementAndGet();
        this.type = type;
        this.text = text;
        this.Sender = sender;
        this.receiver = receiver;
        this.acknowledged = false;
        this.msgTreatmentState = false;

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

    public String getText() {
        return text;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged){
        this.acknowledged=acknowledged;
    }

    public void setMsgTreatmentState(boolean msgTreatmentState) {
        this.msgTreatmentState = msgTreatmentState;
    }
}

