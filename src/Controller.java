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

    // l'utilisateur s'abonne en tant qu'agent tout en utilisant un nom d'agent unique.
    public void subscribe(Agent agent){
        //vérifier le nom de l'agent s'il est déjà inscrit
        for (Agent agentName: listAgents){
            if(agentName.equals(agent.getName()) ){
                throw new IllegalArgumentException("Agent name is already subscribed by other! Please chose a new name!");
            };
        }
        // Sinon, ajouter le nom de l'agent à la liste des agents et afficher un message à l'utilisateur
        listAgents.add(agent);
        agent.setStateOnline(true);
        loggerConsole.info("Congratulations! Agent "+agent.getName()+" is subscribed successfully!");
        fileLogger.info("Subscribed agent: " + agent.getName());

    }

    // Cette méthode est utilisée pour se désabonner l'agent.
    public void unsubscribe(Agent agent){
        // L'utilisateur ne peut se désinscrire que tant que son nom figure dans la liste des agents，
        // Si le nom existe déjà, le système affichera un message d'erreur
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

    // Chercher un agent spécifique en utilisant son nom comme un paramètre, s'il existe,
    // retourner le nom, sinon retourner null
    public Agent findAgentByName(String name) {
        for (Agent agentFind : listAgents) {
            if (agentFind.getName().equals(name)){
                return agentFind;
            }
        }
        return null;
        }


    // La fonction 'isAgentExist' appelle la fonction 'findAgentByName'. Nous comparons le résultat qui renvoie en
    // fonction 'findAgentByName' avec la valeur 'null'. Lorsque la méthode 'findAgentByName' renvoie null,
    // cela signifie que l'agent qui l'utilisateur que nous le cherchons n'existe pas, donc la fonction 'isAgentExist'
    // retournera un booléen

    public boolean isAgentExist(String nameAgentCheck){
        return findAgentByName(nameAgentCheck) !=null;
    }

    public void addMsgToListInTransit(MessageText msg){

        listMessageInTransit.add(msg);

    }


    public synchronized void processMsgInTransit(int time){
        Iterator<MessageText> iterator = listMessageInTransit.iterator();
       // System.out.println(time);
        while (iterator.hasNext()) {
            MessageText message = iterator.next();
            boolean receiverExist = isAgentExist(message.getReceiver());
            Agent receiverAgent= findAgentByName(message.getReceiver());

            //Simuler le scénario où on perd les deux premiers ACK message par rapport au message 2
            if(time <= 2 & message.getType() == msgType.ACK && message.getRelatedMessage()==2 ){
                message.setMessageState(false);
            }else if(time > 2 & message.getType() == msgType.ACK ){
                message.setMessageState(true);
            }

            // Si les messages ne sont pas perdus et peuvent être envoyées
            if (message.isMessageActive() == true){

                // Si le receveur existe et qu'il est en ligne, envoyer un message au receveur
                if (receiverExist ==true && (receiverAgent.isStateOnline()==true)) {

                // Afficher des informations sur Console et dans le log ficher
                loggerConsole.info("Message " + message.getNumMessage() + " sent to " + message.getReceiver());
                fileLogger.info("Message " + message.getNumMessage() + " sent to " + message.getReceiver());
                transferMessage(message,receiverAgent);
                // Une fois le message traité normalement, nous le supprimons de la liste 'listMessageInTransit'
                iterator.remove();

                // s'il n'y a pas de receveur et l'expéditeur est toujours exist,
                // créer un message de type noReceiver et renvoyer-le à l'expéditeur.
            }else if(receiverExist==false) {
                MessageText noReceiverMessage = new MessageText(message.getSender(),null, msgType.noReceiver, "Receiver does not exist");
                Agent sender = findAgentByName(message.getSender());
                if (sender != null) {
                    sender.receiveMessage(noReceiverMessage);

                    loggerConsole.debug("Receiver: " + message.getReceiver() +" do not existe. Please try again later. ");
//                            " does not exist. Sent NoReceiver message to " + message.getSender());
                    fileLogger.debug("Receiver: " + message.getReceiver() +" do not existe");
                }
                iterator.remove();
            }
                // le message est perdu, le système va afficher des informations sur Console et log fichier
            }else {
                if(message.getType() == msgType.ACK){
                    loggerConsole.debug("Sorry, message "+message.getNumMessage()+ "( " + message.getType() +
                            ") is lost! (related to message " + message.getRelatedMessage() +") ");
                    fileLogger.debug("Sorry, message "+message.getNumMessage()+ "( " + message.getType() +
                            ") is lost! (related to message " + message.getRelatedMessage() +") ");
                }
                else{
                    loggerConsole.debug("Sorry, message "+message.getNumMessage()+ "( " + message.getType() +
                            ") is lost!");
                    fileLogger.debug("Message "+message.getNumMessage()+"( " + message.getType() +
                            ") is lost!");}}
            }

    }


    // Cette méthode peut transfer des messages au receveur.
    public void transferMessage(MessageText message,Agent receiver){
        receiver.receiveMessage(message);
    }

    public void run() {

        int time = 0;  // Combien de fois le contrôleur traite les messages
        while (true) {
            System.out.println("\n");
            System.out.println("Controller start processing messages (time: " + (time +1) +" )." );
            processMsgInTransit(time++);
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
