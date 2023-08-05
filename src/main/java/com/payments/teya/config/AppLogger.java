package com.payments.teya.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class AppLogger {
    public static Logger getLogger(String className) {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("src/main/java/resources/logger.properties"));
        } catch (IOException e) {
            System.out.println("Error configuring the logger, default configs will be used!" + e.getMessage());
        }
        return Logger.getLogger(className.getClass().getName());
    }
}
