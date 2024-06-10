import com.example.logging.Logger;
import com.example.logging.loggerConsole;
import com.example.logging.loggerFile;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Initialize loggers
        Logger consoleLogger = new loggerConsole("Controller");
        Logger fileLogger = null;
        try {
            fileLogger = new loggerFile("Controller","log.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create the controller
        Controller controller = new Controller(consoleLogger,fileLogger);

        // Create agents BOB and ALICE
        Agent bob = new Agent("BOB", controller, consoleLogger, fileLogger);
        Agent alice = new Agent("ALICE", controller, consoleLogger, fileLogger);

        // Register the agents with the controller
        controller.subscribe(bob);
        controller.subscribe(alice);

        // Create encoders
//        Encoder encoder = new CodeShiftEncoder();
//        Encoder neutralEncoder = new EncoderNeutre();


        // Create and send specific messages from BOB to ALICE
        MessageText message1 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "Hello ALICE, this is BOB.");
        bob.sendMessage(message1);
        //sendMessageAndWait(bob, alice, message1.getText());

        MessageText message2 = new MessageText(bob.getName(), alice.getName(), msgType.normalText, "Hey!");
        //sendMessageAndWait(bob, alice, message2.getText());
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


        // Create and start the controller thread to handle message exchange
        Thread controllerThread = new Thread(controller);


        // 创建代理和控制器的线程
        Thread bobThread = new Thread(bob);
        Thread aliceThread = new Thread(alice);

        // 启动线程
        bobThread.start();
        aliceThread.start();
        controllerThread.start();

        // 确保主线程等待这些线程完成
        try {
            bobThread.join();
            aliceThread.join();
            controllerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
        // Monitor agents' behavior
//        while (controllerThread.isAlive()) {
//            controller.processMsgInTransit();
//            try {
//                Thread.sleep(1000); // Insert pauses to slow down execution
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    // private static final Object lock = new Object();

    }

