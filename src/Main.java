public class Main {
    public static void main(String[] args) {


        Agent agent1 = new Agent("Bob");

        // set the agent's state to online
        agent1.setStateOnline(true);

        // create a thread for the agent
        Thread agentThread1 = new Thread(agent1);

        // start the agent thread
        agentThread1.start();


        Agent agent2 = new Agent("Anne");

        // set the agent's state to online
        agent2.setStateOnline(true);

        // create a thread for the agent
        Thread agentThread2 = new Thread(agent1);

        // start the agent thread
        agentThread2.start();
    }
}