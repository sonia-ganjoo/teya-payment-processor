package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.exceptions.InvalidInputException;
import org.junit.platform.commons.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import static com.payments.teya.util.Constants.COLON_DELIMITER;
import static com.payments.teya.util.Constants.COMMA_DELIMITER;

/**
 * This class is responsible for splitting strings based on delimiters
 *
 */
public class TokenProcessor {

    Logger logger = AppLogger.getLogger("TokenProcessor");

    /**
     * Method to list of payments from the payments string
     * @param tokenizedPayments payments in string format
     * @return list of strings of payments after separating using Comma delimiter
     */
    public List<String> getPaymentsFromString(String tokenizedPayments) throws InvalidInputException {
        return getTokensFromString(tokenizedPayments, COMMA_DELIMITER);
    }

    /**
     * Method to split any string based on given delimiter
     * @param tokenizedString - tokens string
     * @param delimiter - delimiter to split the token string
     * @return list of string after splitting using given delimiter
     */
    public List<String> getTokensFromString(String tokenizedString, String delimiter) throws InvalidInputException {
        if (StringUtils.isBlank(tokenizedString) || StringUtils.isBlank(delimiter)) {
            throw new InvalidInputException("Received empty payments string!");
        }

        List<String> tokenList = new ArrayList<>();

        try {
            StringTokenizer stringTokenizer = new StringTokenizer(tokenizedString, delimiter);

            while (stringTokenizer.hasMoreTokens()) {
                tokenList.add(stringTokenizer.nextToken());
            }
        } catch (Exception e) {
            logger.severe("Error parsing the string!" + e.getMessage());
        }

        return tokenList;
    }

    /**
     * Method to parse funds string to a map
     * @param tokenizedFunds funds as tokenized string
     * @return hashmap for each currency and funds available for that currency
     */
    public Map<String, Double> getFundsFromString(String tokenizedFunds) throws InvalidInputException {
        List<String> tokenizedFundsList = getTokensFromString(tokenizedFunds, COMMA_DELIMITER);
        Map<String, Double> fundsMap = new HashMap<>();

        for (String payment : tokenizedFundsList) {
            String[] paymentTokens = payment.split(COLON_DELIMITER);
            // get a map with key as currency and funds as value
            fundsMap.put(paymentTokens[0], Double.valueOf(paymentTokens[1]));
        }

        return fundsMap;
    }
}
