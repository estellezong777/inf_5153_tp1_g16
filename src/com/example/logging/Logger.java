package com.example.logging;
import java.util.logging.Level;

public abstract class Logger {
    protected final loggerLevel level;

    // Le nom du composant du message de journal généré
    protected final String serviceName;

    public Logger(loggerLevel LoggerLevel,String serviceName) {
        this.level =LoggerLevel;
        this.serviceName=serviceName;
    }

    public abstract void log(loggerLevel level, String messagePrint);

    public void info(String msg){
        log(loggerLevel.INFO," ( "+ serviceName +" ) " + msg);
    }
    public void debug(String msg){
        log(loggerLevel.DEBUG," ( "+ serviceName +" ) " + msg);
    }
}
