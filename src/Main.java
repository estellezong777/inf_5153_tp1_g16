import com.example.logging.Logger;
import com.example.logging.loggerConsole;
import com.example.logging.loggerFile;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        //initiate loggers
        Logger consoleLogger = new loggerConsole("Controller");
        Logger fileLogger = null;
        try {
            fileLogger = new loggerFile("Controller","log.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Controller controller = new Controller(consoleLogger,fileLogger);
        Agent bob = new Agent("BOB", controller, consoleLogger, fileLogger);
        Agent alice = new Agent("ALICE", controller, consoleLogger, fileLogger);

        // register the agents with the controller
        controller.subscribe(bob);
        controller.subscribe(alice);



        //Scenario: create 10 messages from BOB to ALICE
        MessageText message1 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "Hello ALICE, this is BOB.");
        bob.sendMessage(message1);

        MessageText message2 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "Hey!");
        bob.sendMessage(message2);

        MessageText message3 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "What's up!");
        message3.setMessageState(false);
        bob.sendMessage(message3);

        MessageText message4 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "Any plans for summer holidays?.");
        message4.setMessageState(false);
        bob.sendMessage(message4);

        MessageText message5 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "You want to go to Toronto?");
        bob.sendMessage(message5);

        MessageText message6 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "Or you prefer play in Disney in the states?");
        bob.sendMessage(message6);

        MessageText message7 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "Or we can have a BBQ party in Montreal");
        bob.sendMessage(message7);

        MessageText message8 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "Please let me know");
        message8.setMessageState(false);
        bob.sendMessage(message8);

        MessageText message9 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "I am going to the bed now");
        bob.sendMessage(message9);

        MessageText message10 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "Bye. Looking forward to your reply.");
        bob.sendMessage(message10);


        // create threads
        Thread controllerThread = new Thread(controller);
        Thread bobThread = new Thread(bob);
        Thread aliceThread = new Thread(alice);

        // start the threads
        bobThread.start();
        aliceThread.start();
        controllerThread.start();

        // create the waiting list
        try {
            bobThread.join();
            aliceThread.join();
            controllerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    }

