import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class Controller {
    private final List<MessageText> listMessageInTransit;
    private final List<Agent> listAgents;
    private final Logger logger;

    public Controller(Logger logger){
        this.listAgents = new ArrayList<>();
        this.listMessageInTransit= new ArrayList<>();
        this.logger=logger;
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
        logger.info("Congratulations! Agent "+agent.getName()+" is subscribed successfully!");

    }

    public void unsubscribe(Agent agent){
        //the user only can unsubscribe while his/her name is in the agent list.
        for (Agent agentName: listAgents){
            if(agentName.equals(agent.getName()) ){
                listAgents.remove(agent);
                logger.info("Agent " + agent.getName() + " unsubscribed successfully!");
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
                receiver.receiveMessage(message);
                //add logger maybe
//                logger.info("Message " + message.getNumMessage() + " sent to " + message.getReceiver());
                iterator.remove();
                //if no receiver, create a message with type noReceiver and back send to the sender
            } else {
                MessageText noReceiverMessage = new MessageText(message.getSender(),null, msgType.noReceiver, "Receiver does not exist");
                Agent sender = findAgentByName(message.getSender());
                if (sender != null) {
                    sender.receiveMessage(noReceiverMessage);
                    //maybe a logger
//                    logger.debug("Receiver " + message.getReceiver() +
//                            " does not exist. Sent NoReceiver message to " + message.getSender());
                }
                iterator.remove();
            }
        }
    }
    }











