package com.payments.teya;

import com.payments.teya.processor.InputProcessor;

/**
 * Main class for the program that reads input string
 */
public class AppMain {

    public static void main(String[] args) {
        InputProcessor inputProcessor = new InputProcessor();
        inputProcessor.processInput();
    }
}
