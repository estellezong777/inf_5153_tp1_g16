package com.example.logging;

public abstract class Logger {
    public enum loggerLevel {
        INFO,
        DEBUG
    }
//    protected loggerLevel level;

    // Le nom du composant du message de journal généré, nous utilisons "Controller" par défaut
    protected final String serviceName;


    public Logger(String serviceName) {
        this.serviceName=serviceName;
    }

    //Cette méthode prend un level et messagePrint comme des paramètres. Nous l'utilisons pour imprimer des messages
    // dans Console et le log fichier.
    public abstract void log(loggerLevel level, String messagePrint);


    // La méthode 'public void info' est appelée quand un message est envoyé avec succès.
    // Et la méthode 'public void debug' est appelée quand un message est envoyé avec échec.
    // Le niveau INFO est plus important. Mais le niveau DEBUG a moins de priorité, donc il imprime des messages qui
    // indiquent le problème et le problème d'envoi dans Console.
    public void info(String msg){
        log(loggerLevel.INFO," ( "+ serviceName +" ) " + msg);
    }

    public void debug(String msg){
        log(loggerLevel.INFO," ( "+ serviceName +" ) " + msg);
        log(loggerLevel.DEBUG," ( "+ serviceName +" ) " + msg);

    }
}
