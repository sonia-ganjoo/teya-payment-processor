package com.payments.teya;

import com.payments.teya.config.AppLogger;
import com.payments.teya.processor.InputProcessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class AppMain {
    static Logger logger = AppLogger.getLogger("AppMain");

    public static void main(String[] args) {
        String input = "GBP:100,EUR:200,CZK:1000|743:EUR:5.76,932:GBP:32.10,90" +
                "9:CZK:223.26,23:CZK:890.22,902:GBP:58.23,89:EUR:104.25" +
                ",663:EUR:97.43,902:EUR:20.01";

        if (!input.isBlank()) {
            InputProcessor inputProcessor = new InputProcessor();
            inputProcessor.generateBatchPayments(input);
        } else {
            logger.warning("No input found!");
        }
    }
}
