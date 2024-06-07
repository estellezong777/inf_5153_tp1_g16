package com.example.logging;


public class loggerConsole extends Logger{
    public loggerConsole( String serviceName) {
        super( serviceName);
    }

    @Override
    public void log(loggerLevel level, String messagePrint) {
        System.out.println("Message Level: "+level +"."+"Here is the message: "+messagePrint);


    }


}
