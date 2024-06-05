public abstract class Logger {
    protected final loggerLevel level;

    public Logger(loggerLevel LoggerLevel) {
        this.level =LoggerLevel;
    }

    public abstract void log(loggerLevel level, String messagePrint);

}
