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
    private Encoder encoderNeutre = new EncoderNeutre();

    public Agent(String nomAgent, Controller controller, Logger loggerConsole, Logger fileLogger) {
        this.nomAgent = nomAgent;
        this.sendmsgList = new ArrayList<>();
        this.receivedmsgList = new ArrayList<>();
        this.controller = controller;
        this.loggerConsole = loggerConsole;
        this.fileLogger = fileLogger;
    }


    //Cette méthode permet au destinataire d'obtenir le message texte normal envoyé par l'expéditeur
    public void receiveMessage(MessageText msg) {
        if (msg.getType() == msgType.normalText) {
            String textToDecode = msg.getText();
            //Parce que le scénario a besoin d'un encodeur neutre, nous supposons
            // ici que le premier message utilise encoderNeutre.
            if (msg.getNumMessage()==1){
                msg.setText(encoderNeutre.decode(textToDecode));
            }
            //Pour les messages contenant d'autres numéros de série,
            // nous appelons 'decode' pour décrypter les informations cryptées afin que
            // le destinataire puisse obtenir un texte normal.
            else {
                msg.setText(encoder.decode(textToDecode));}
        }
        receivedmsgList.add(msg);

        //Si le type d'information est ACK ou texte normal, les informations correspondantes seront affichées
        // dans la console et le fichier.
        if (msg.getType() == msgType.ACK || msg.getType() == msgType.normalText) {
            Logger.loggerLevel logLevel = Logger.loggerLevel.INFO;
            String messagePrint = msg.getReceiver() + " received a message " + msg.getType() + "(" + msg.getNumMessage() + ") from: "
                    + msg.getSender()+" Message content: "+msg.getText();
            loggerConsole.log(logLevel, messagePrint);
            fileLogger.info(messagePrint);
        }
        //Si le type d'information est noReceiver, ce qui signifie qu'il n'y a pas de récepteur,
        // les informations seront également affichées dans la console et le fichier pour informer
        // l'utilisateur que la transmission des informations a échoué.
        else if (msg.getType() == msgType.noReceiver) {
            Logger.loggerLevel logLevel = Logger.loggerLevel.DEBUG;
            String messagePrint = msg.getReceiver() + " doesn't exist. " + "\n Message type is: " + msg.getType() + "." +
                    "\n" + "The message " + msg.getNumMessage() + " failed to send by " + msg.getSender();
            loggerConsole.log(logLevel, messagePrint);
            fileLogger.info(messagePrint);
        }
    }


    //Grâce à cette méthode, l'agent peut crypter les informations qui doivent être envoyées
    // et les envoyer au receveur. Ensuite, les informations cryptées seront ajoutées à la liste
    // de transit en attente de traitement, et la console et le fichier afficheront également le texte
    // correspondant pour demander l'état d'envoi des informations.
    public void sendMessage(MessageText msg) {
        //Pour message1 : Encodeur neutre
        if (msg.getType() == msgType.normalText) {
            sendmsgList.add(msg);

            String textToEncode = msg.getText();
            ////Comme ci-dessus, nous suivons ici les exigences du scénario et supposons
            // message 1 en utilisant encoderNeutre
            if (msg.getNumMessage()==1){

                msg.setText(encoderNeutre.encode(textToEncode));

            }else {
                msg.setText(encoder.encode(textToEncode));}
        }

        controller.addMsgToListInTransit(msg);

        Logger.loggerLevel logLevel = Logger.loggerLevel.INFO;
        String messagePrint = msg.getSender() + " sent a message " + msg.getType() + "( " + msg.getNumMessage() +
                ") to: " + msg.getReceiver();
        if (msg.getType() == msgType.ACK){
            messagePrint = msg.getSender() + " sent a message " + msg.getType() + "( " + msg.getNumMessage() +
                    ") to: " + msg.getReceiver() + " who had sent message " + msg.getRelatedMessage() ;
        }
        loggerConsole.log(logLevel, messagePrint);
        fileLogger.info(messagePrint);
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
                    messageACK.setRelatedMessage(msg.getNumMessage());
                    sendMessage(messageACK);
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
        notify(); // signaler au Contrôleur pour procéder
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
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
