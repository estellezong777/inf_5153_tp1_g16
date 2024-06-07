package com.example.logging;

public abstract class Logger {
    public enum loggerLevel {
        INFO,
        DEBUG
    }
    protected loggerLevel level;

    // Le nom du composant du message de journal généré
    protected final String serviceName;


    public Logger(String serviceName) {
        this.serviceName=serviceName;
    }

    public abstract void log(loggerLevel level, String messagePrint);

    public void info(String msg){
        log(loggerLevel.INFO," ( "+ serviceName +" ) " + msg);
    }
    public void debug(String msg){
        log(loggerLevel.INFO," ( "+ serviceName +" ) " + msg);
        log(loggerLevel.DEBUG," ( "+ serviceName +" ) " + msg);

    }
}
