import java.util.ArrayList;
import java.util.List;

public class Agent implements Runnable {

    private String nomAgent;
    private List<MessageText> checkList ;
    private List<MessageText> sentmsgList;
    private List<MessageText> receivedmsgList;

    private boolean stateOnline;
    Controller controller;

    //constructor
    public Agent(String nomAgent){
        this.nomAgent = nomAgent;
        this.checkList = new ArrayList<>();
        this.sentmsgList = new ArrayList<>();
        this.receivedmsgList= new ArrayList<>();
    }

    public void sendMessage(MessageText msg){
        if(msg!=null){
            //controller.addMessageToList();
            sentmsgList.add(msg);
            checkList.add(msg);
        }
    }

    public void receiveMessage(MessageText msg){
        receivedmsgList.add(msg);
        // checkList.add(msg.getNumMessge());
    }

    public boolean isStateOnline() {
        return stateOnline;
    }

    public void setStateOnline(boolean stateOnline) {
        this.stateOnline = stateOnline;
    }

    public void processReceivedMessages(MessageText msg){

        switch(msg.getMsgType()){
            case ACK:
                for (int i = 0; i < checkList.size(); i++) {
                    if(checkList.get(i).getNumMessge() == msg.getNumMessge()){
                        checkList.remove(i);
                    }
                }
                receiveMessage(msg); ///???
                break;

            case normalText:
                receiveMessage(msg);
                break;

            case noReceiver:
                for (int i = 0; i < checkList.size(); i++) {
                    if(checkList.get(i).getNumMessge() == msg.getNumMessge()){
                        checkList.remove(i);
                    }
                }
                //receiveMessage(msg); ///???
                break;
            default:

        }
    }

    public String getName() {
        return nomAgent;
    }

    @Override
    public void run(){
        while(true){
            if (checkList.size()>0){
                //resend to controller the messages still in the checkList
                for (MessageText msg: checkList) {
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
