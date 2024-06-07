package com.example.logging;


import java.io.*;

public class loggerFile extends Logger implements AutoCloseable{
    private final PrintWriter writer;

    public loggerFile( String serviceName, String fileName) throws IOException{
        super(serviceName);
        this.writer = new PrintWriter(new FileWriter(fileName,true));
    }
    @Override
    public void log(loggerLevel level, String message) {
        writer.println("[" + level + "] " + message);
        writer.flush();
    }

    @Override
    public void close() {
        writer.close();
    }
}
