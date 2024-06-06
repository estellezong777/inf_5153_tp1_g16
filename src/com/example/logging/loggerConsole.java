package com.example.logging;


public class loggerConsole extends Logger{
    public loggerConsole(loggerLevel loggerLevel, String serviceName) {
        super(loggerLevel, serviceName);
    }

    @Override
    public void log(loggerLevel level, String messagePrint) {
        System.out.println("Lever: "+level +"."+"Here is the message: "+messagePrint);


    }


}
