package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.exceptions.InvalidInputException;
import com.payments.teya.util.FileReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

import static com.payments.teya.util.Constants.LINE_SEPARATOR;

/**
 * class to read input from the file
 *  and get final result
 */
public class InputProcessor {
    static Logger logger = AppLogger.getLogger("InputProcessor");

    public InputProcessor() {}

    /**
     * read from file and get result string
     */
    public void processInput() {
        FileReader fileReader = new FileReader();
        try (FileInputStream fileInputStream = new FileInputStream(fileReader.getResourceFile("resources/application.properties"))) {
            Properties props = new Properties();
            props.load(fileInputStream);

            Optional<String> property = Optional.ofNullable(props.getProperty("inputFilePath"));

            if (property.isPresent()) {
                Path path = Paths.get(property.get());

                try (BufferedReader reader = Files.newBufferedReader(path)) {

                    String line = reader.readLine();

                    if (!line.isBlank()) {
                        PaymentProcessor paymentProcessor = new PaymentProcessor();
                        String result = paymentProcessor.generateBatchPayments(line);
                        // printing result here for the sake of convenience to see the
                        // output when the program is run from command line
                        System.out.println("Output String:::" + LINE_SEPARATOR + result);
                    } else {
                        throw new InvalidInputException("Empty input file!");
                    }
                }
            } else {
                throw new InvalidInputException("Path property not found in the properties file!");
            }
        } catch (InvalidInputException e) {
            logger.severe(e.getMessage());
        } catch (IOException e) {
            logger.severe("Error reading input file! " + e.getMessage());
        }
    }
}
