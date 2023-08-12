package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.exceptions.InvalidInputException;
import org.junit.platform.commons.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * class to read input from the file
 *  and get final result
 */
public class InputProcessor {
    static Logger logger = AppLogger.getLogger("InputProcessor");

    /**
     * read from file and get result string
     */
    public void processInput() {
        try (FileInputStream fileInputStream = new FileInputStream("src\\main\\java\\resources\\application.properties")) {
            Properties props = new Properties();
            props.load(fileInputStream);

            Optional<String> property = Optional.ofNullable(props.getProperty("inputFilePath"));

            if (property.isPresent()) {
                Path path = Paths.get(property.get());

                BufferedReader reader = Files.newBufferedReader(path);
                String line = reader.readLine();

                if (!StringUtils.isBlank(line)) {
                    PaymentProcessor paymentProcessor = new PaymentProcessor();
                    paymentProcessor.generateBatchPayments(line);
                } else {
                    throw new InvalidInputException("Empty input file!");
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
