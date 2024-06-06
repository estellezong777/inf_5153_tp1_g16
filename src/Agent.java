import java.util.ArrayList;
import java.util.List;
import com.example.logging.Logger;
import com.example.logging.loggerLevel;

public class Agent implements Runnable {

    private String nomAgent;
    private List<MessageText> sendmsgList;
    private List<MessageText> receivedmsgList;

    private boolean stateOnline;
    Controller controller;

    Logger logger;

    //constructor
    public Agent(String nomAgent){
        this.nomAgent = nomAgent;
        this.sendmsgList = new ArrayList<>();
        this.receivedmsgList= new ArrayList<>();
    }

    public void sendMessage(MessageText msg){
        if(msg.getType() == msgType.normalText) {
            sendmsgList.add(msg); //How about ack?
            //if message.type == ack???, i should wait until i receive another ack?
        }
            controller.addMsgToListInTransit(msg);
            //log
            loggerLevel logLevel = loggerLevel.INFO;
            String messagePrint = msg.getSender() + "sent a message " + msg.getType() +"( " + msg.getNumMessage()+
                    ") from: " + msg.getReceiver();//or controller?
            logger.log(logLevel, messagePrint);



    }

    //TODO:a etre appeler dans controller processInTransit method
    public void receiveMessage(MessageText msg){
        receivedmsgList.add(msg);
        //log
        if(msg.getType() == msgType.ACK||msg.getType() == msgType.normalText) {
            loggerLevel logLevel = loggerLevel.INFO;
            String messagePrint = msg.getReceiver() + "received a message "+msg.getType()+"( "+  msg.getNumMessage()+") from: "
                    + msg.getSender();
            //logger.log(logLevel, messagePrint); maybe appeler d'autres methodes
        } else if(msg.getType() == msgType.noReceiver){
            loggerLevel logLevel = loggerLevel.DEBUG;
            String messagePrint = msg.getReceiver() + "received a message "+msg.getType()+"( "+  msg.getNumMessage() +": the receiver doesn't exist";
                //    + msg.getSender();
            //logger.log(logLevel, messagePrint);
        }
    }

    public boolean isStateOnline() {
        return stateOnline;
    }

    public void setStateOnline(boolean stateOnline) {
        this.stateOnline = stateOnline;
    }

    public void processReceivedMessages( ){
        for (MessageText msg: receivedmsgList){
            switch(msg.getType()){
                case ACK:
                    for (int i = 0; i < sendmsgList.size(); i++) {
                        if(sendmsgList.get(i).getNumMessage() == msg.getNumMessage()){
                            sendmsgList.remove(i);
                        }
                    }
                    break;

                case normalText:
                    MessageText messageACK = new MessageText(msg.getReceiver(),msg.getSender(),msgType.ACK,"ACK");
                    controller.addMsgToListInTransit(messageACK);
                    break;

                case noReceiver:
                    for (int i = 0; i < sendmsgList.size(); i++) {
                        if(sendmsgList.get(i).getNumMessage() == msg.getNumMessage()){
                            sendmsgList.remove(i);
                        }
                    }
                    break;
                default:
                    System.out.println("Invalid type of messageText");
                    break;
            }
        }
    }

    public String getName() {
        return nomAgent;
    }

    @Override
    public void run(){
        while(true){
            if (sendmsgList.size()>0){
                //resend to controller the messages still in the sendmsgList
                for (MessageText msg: sendmsgList) {
                    sendMessage(msg);

                }
            }

            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
