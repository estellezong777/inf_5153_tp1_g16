import com.example.logging.Logger;
import java.util.ArrayList;
import java.util.List;

public class Agent implements Runnable {

    private String nomAgent;
    private List<MessageText> sendmsgList;
    private List<MessageText> receivedmsgList;

    private boolean stateOnline = false;
    private final Controller controller;

    private final Logger loggerConsole;
    private final Logger fileLogger;

    private Encoder encoder = new CodeShiftEncoder();

    public Agent(String nomAgent, Controller controller, Logger loggerConsole, Logger fileLogger) {
        this.nomAgent = nomAgent;
        this.sendmsgList = new ArrayList<>();
        this.receivedmsgList = new ArrayList<>();
        this.controller = controller;
        this.loggerConsole = loggerConsole;
        this.fileLogger = fileLogger;
    }

    public void sendMessage(MessageText msg) {
        if (msg.getType() == msgType.normalText) {
            sendmsgList.add(msg);
            String textToEncode = msg.getText();
            msg.setText(encoder.encode(textToEncode));
        }
        controller.addMsgToListInTransit(msg);
        Logger.loggerLevel logLevel = Logger.loggerLevel.INFO;
        String messagePrint = msg.getSender() + " sent a message " + msg.getType() + "( " + msg.getNumMessage() +
                ") to: " + msg.getReceiver();
        loggerConsole.log(logLevel, messagePrint);
        fileLogger.info(messagePrint);
    }

    public void receiveMessage(MessageText msg) {
        if (msg.getType() == msgType.normalText) {
            String textToDecode = msg.getText();
            msg.setText(encoder.decode(textToDecode));
        }
        receivedmsgList.add(msg);
        if (msg.getType() == msgType.ACK || msg.getType() == msgType.normalText) {
            Logger.loggerLevel logLevel = Logger.loggerLevel.INFO;
            String messagePrint = msg.getReceiver() + " received a message " + msg.getType() + "( " + msg.getNumMessage() + ") from: "
                    + msg.getSender();
            loggerConsole.log(logLevel, messagePrint);
            fileLogger.info(messagePrint);
        } else if (msg.getType() == msgType.noReceiver) {
            Logger.loggerLevel logLevel = Logger.loggerLevel.DEBUG;
            String messagePrint = msg.getReceiver() + " doesn't exist. " + "\n Message type is: " + msg.getType() + "." +
                    "\n" + "The message " + msg.getNumMessage() + " failed to send by " + msg.getSender();
            loggerConsole.log(logLevel, messagePrint);
            fileLogger.info(messagePrint);
        }
    }

    public boolean isStateOnline() {
        return stateOnline;
    }

    public void setStateOnline(boolean stateOnline) {
        this.stateOnline = stateOnline;
    }

    public synchronized void processReceivedMessages() {
        for (MessageText msg : receivedmsgList) {
            switch (msg.getType()) {
                case ACK:
                    sendmsgList.removeIf(m -> m.getNumMessage() == msg.getNumMessage());
                    break;
                case normalText:
                    MessageText messageACK = new MessageText(msg.getReceiver(), msg.getSender(), msgType.ACK, "ACK");
                    controller.addMsgToListInTransit(messageACK);
                    break;
                case noReceiver:
                    sendmsgList.removeIf(m -> m.getNumMessage() == msg.getNumMessage());
                    break;
                default:
                    System.out.println("Invalid type of messageText");
                    break;
            }
        }
        receivedmsgList.clear();
        notify(); // Notify the controller to proceed
    }

    public String getName() {
        return nomAgent;
    }

    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                processReceivedMessages();
                try {
                    wait(); // Wait until notified
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
