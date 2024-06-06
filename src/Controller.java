import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.example.logging.Logger;
import com.example.logging.loggerLevel;
public class Controller {
    private final List<MessageText> listMessageInTransit;
    private final List<Agent> listAgents;

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
        loggerConsole.info("Congratulations! Agent "+agent.getName()+" is subscribed successfully!");
        fileLogger.info("Subscribed agent: " + agent.getName());

    }

    public void unsubscribe(Agent agent){
        //the user only can unsubscribe while his/her name is in the agent list.
        for (Agent agentName: listAgents){
            if(agentName.equals(agent.getName()) ){
                listAgents.remove(agent);
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
    public void processMsgInTransit(){
        Iterator<MessageText> iterator = listMessageInTransit.iterator();
        while (iterator.hasNext()) {
            MessageText message = iterator.next();
            Agent receiver = findAgentByName(message.getReceiver());
            //if receiver exist, send message to receiver
            if (receiver != null) {
                transferMessage(message,receiver);
                //add logger maybe
                loggerConsole.info("Message " + message.getNumMessage() + " sent to " + message.getReceiver());
                fileLogger.info("Message " + message.getNumMessage() + " sent to " + message.getReceiver());
                iterator.remove();
                //if no receiver, create a message with type noReceiver and back send to the sender
            } else {
                MessageText noReceiverMessage = new MessageText(message.getSender(),null, msgType.noReceiver, "Receiver does not exist");
                Agent sender = findAgentByName(message.getSender());
                if (sender != null) {
                    sender.receiveMessage(noReceiverMessage);
                    //maybe a logger
                   loggerConsole.debug("Receiver: " + message.getReceiver() +"do not existe. Please try again later. ");
//                            " does not exist. Sent NoReceiver message to " + message.getSender());
                    fileLogger.info("Receiver: " + message.getReceiver() +"do not existe");
                }
                iterator.remove();
            }
        }
    }
    public void transferMessage(MessageText message,Agent receiver){
        receiver.receiveMessage(message);
    }

    public void run() {
        while (true) {
            processMsgInTransit();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                loggerConsole.debug("System interrupted: " + e.getMessage());
                fileLogger.debug("System interrupted: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    }
