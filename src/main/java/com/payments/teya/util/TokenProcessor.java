package com.payments.teya.util;

import com.payments.teya.config.AppLogger;

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
    private StringTokenizer stringTokenizer;

    /**
     * Method to list of payments from the payments string
     * @param tokenizedPayments
     * @return the string of payments after separating using Comma delimiter
     */
    public List<String> getPaymentsList(String tokenizedPayments) {
        return getTokensFromString(tokenizedPayments, COMMA_DELIMITER);
    }

    /**
     * Method to split any string based on given delimiter
     * @param tokenizedString
     * @param delimiter
     * @return list of string after splitting using given delimiter
     */
    public List<String> getTokensFromString(String tokenizedString, String delimiter) {
        List<String> tokenList = new ArrayList<>();
        try {
            stringTokenizer = new StringTokenizer(tokenizedString, delimiter);

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
     * @param tokenizedFunds
     * @return hashmap for each currency and funds available for that currency
     */
    public Map<String, Double> getFundsFromTokenList(String tokenizedFunds) {
        List<String> tokenizedFundsList = getTokensFromString(tokenizedFunds, COMMA_DELIMITER);
        Map<String, Double> fundsMap = new HashMap<>();

        try {
            for (String payment : tokenizedFundsList) {
                String[] paymentTokens = payment.split(COLON_DELIMITER);
                // get a map with key as currency and funds as value
                fundsMap.put(paymentTokens[0], Double.valueOf(paymentTokens[1]));
            }
        } catch (Exception e) {
            logger.severe("Invalid funds file!" + e.getMessage());
        }
        return fundsMap;
    }
}
