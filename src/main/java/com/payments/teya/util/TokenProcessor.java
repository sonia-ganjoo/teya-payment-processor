package com.payments.teya.util;

import com.payments.teya.config.AppLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class TokenProcessor {
    private static final String COMMA_DELIMITER = ",";
    private static final String COLON_DELIMITER = ":";
    Logger logger = AppLogger.getLogger("TokenProcessor");
    private StringTokenizer stringTokenizer;
    public List<String> getPaymentsList(String tokenizedPayments) {
        return getTokensFromString(tokenizedPayments, COMMA_DELIMITER);
    }

    public List<String> getTokensFromString(String tokenizedString, String delimiter) {
        stringTokenizer = new StringTokenizer(tokenizedString, delimiter);
        List<String> tokenList = new ArrayList<>();

        while(stringTokenizer.hasMoreTokens()) {
            tokenList.add(stringTokenizer.nextToken());
        }

        return tokenList;
    }

    public Map<String, Double> getFundsFromTokenList(String tokenizedFunds) {
        List<String> tokenizedFundsList = getTokensFromString(tokenizedFunds, COMMA_DELIMITER);
        Map<String, Double> fundsMap = new HashMap<>();

        try {
            for (String payment : tokenizedFundsList) {
                String[] paymentTokens = payment.split(COLON_DELIMITER);
                fundsMap.put(paymentTokens[0], Double.valueOf(paymentTokens[1]));
            }
        } catch (Exception e) {
            logger.severe("Invalid funds file!" + e.getMessage());
        }
        return fundsMap;
    }
}
