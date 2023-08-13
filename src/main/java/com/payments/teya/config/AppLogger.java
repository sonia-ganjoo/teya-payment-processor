package com.payments.teya.config;

import com.payments.teya.util.FileReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class AppLogger {
    public static Logger getLogger(String className) {
        try {
            FileReader fileReader = new FileReader();
            LogManager.getLogManager().readConfiguration(new FileInputStream(fileReader.getResourceFile("resources/logger.properties")));
        } catch (IOException e) {
            System.out.println("Error configuring the logger, default configs will be used! " + e.getMessage());
        }
        return Logger.getLogger(className.getClass().getName());
    }
}
