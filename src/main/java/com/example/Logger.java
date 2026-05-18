package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


//singleton
public class Logger {

    private static Logger instance = null;

    private Logger() {
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }

        return instance;
    }

    public void log(String msg, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(msg);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
