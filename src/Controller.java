import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.example.logging.Logger;
public class Controller implements Runnable{
    private  List<MessageText> listMessageInTransit;
    private  List<Agent> listAgents;

    private final Logger loggerConsole;
    private final Logger fileLogger;

    public Controller(Logger loggerConsole, Logger fileLogger){
        this.listAgents = new ArrayList<>();
        this.listMessageInTransit= new ArrayList<>();
        this.loggerConsole=loggerConsole;
        this.fileLogger=fileLogger;
    }

    // subscribe as an agent while the agent name is unique.
    public void subscribe(Agent agent){
        //check the name if it's already in used
        for (Agent agentName: listAgents){
            if(agentName.equals(agent.getName()) ){
                throw new IllegalArgumentException("Agent name is already subscribed by other! Please chose a new name!");
            };
        }
        // if not, add the agent name to the agents list and print a message to the user
        listAgents.add(agent);
        agent.setStateOnline(true);
        loggerConsole.info("Congratulations! Agent "+agent.getName()+" is subscribed successfully!");
        fileLogger.info("Subscribed agent: " + agent.getName());

    }

    public void unsubscribe(Agent agent){
        //the user only can unsubscribe while his/her name is in the agent list.
        for (Agent agentName: listAgents){
            if(agentName.equals(agent.getName()) ){
                listAgents.remove(agent);
                agent.setStateOnline(false);
                loggerConsole.info("Agent " + agent.getName() + " unsubscribed successfully!");
                fileLogger.info("Unsubscribed agent: " + agent.getName());
        }
        }
        throw new IllegalArgumentException("Agent name doesn't in the agent list! Please check your information!");
    }

    // find a specific agent by enter his/her name,if exist return the name,otherwise return null
    public Agent findAgentByName(String name) {
        for (Agent agentFind : listAgents) {
            if (agentFind.getName().equals(name)){
                return agentFind;
            }
        }
        return null;
        }


    // Fonction 'isAgentExist' call the fonction 'findAgentByName'. We compare the result that return in fonction
    // 'findAgentByName' with the value 'null'. When method findAgentByName return null,it means that the agent that
    // the user want to find doesn't exist, so fonction 'isAgentExist' will return a boolean

    public boolean isAgentExist(String nameAgentCheck){
        return findAgentByName(nameAgentCheck) !=null;
    }

    public void addMsgToListInTransit(MessageText msg){
        listMessageInTransit.add(msg);

    }
    public synchronized void processMsgInTransit(){
        Iterator<MessageText> iterator = listMessageInTransit.iterator();
        while (iterator.hasNext()) {
            MessageText message = iterator.next();
            boolean receiverExist = isAgentExist(message.getReceiver());
            Agent receiverAgent= findAgentByName(message.getReceiver());

            if (message.isMessageActive() == true){ // 信息没有丢失，可以发送
                //if receiver exist and he/her is online, send message to receiver
                if (receiverExist ==true && (receiverAgent.isStateOnline()==true)) {
                transferMessage(message,receiverAgent);

                loggerConsole.info("Message " + message.getNumMessage() + " sent to " + message.getReceiver());
                fileLogger.info("Message " + message.getNumMessage() + " sent to " + message.getReceiver());
                iterator.remove();
                //if no receiver, create a message with type noReceiver and back send to the sender
            }else if(receiverExist==false) {
                MessageText noReceiverMessage = new MessageText(message.getSender(),null, msgType.noReceiver, "Receiver does not exist");
                Agent sender = findAgentByName(message.getSender());
                if (sender != null) {
                    sender.receiveMessage(noReceiverMessage);

                    loggerConsole.debug("Receiver: " + message.getReceiver() +" do not existe. Please try again later. ");
//                            " does not exist. Sent NoReceiver message to " + message.getSender());
                    fileLogger.debug("Receiver: " + message.getReceiver() +"do not existe");
                }
                iterator.remove();
            }
            }else {loggerConsole.debug("Sorry, message "+message.getNumMessage()+" is lost!");}
            fileLogger.debug("Message"+message.getNumMessage()+"is lost");
        }
    }


    public void transferMessage(MessageText message,Agent receiver){
        receiver.receiveMessage(message);
    }

    public void run() {
        while (true) {
            processMsgInTransit();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                loggerConsole.debug("System interrupted: " + e.getMessage());
                fileLogger.debug("Controller interrupted: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    }
