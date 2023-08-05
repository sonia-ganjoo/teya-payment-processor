package com.payments.teya.util;

import com.payments.teya.config.AppLogger;
import com.payments.teya.model.FundDetails;
import com.payments.teya.model.PaymentDetails;

import java.util.ArrayList;
import java.util.List;
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

    public List<String> getFundsList(String tokenizedFunds) {
        return getTokensFromString(tokenizedFunds, COLON_DELIMITER);
    }

    public List<String> getTokensFromString(String tokenizedString, String delimiter) {
        stringTokenizer = new StringTokenizer(tokenizedString, delimiter);
        List<String> tokenList = new ArrayList<>();

        while(stringTokenizer.hasMoreTokens()) {
            tokenList.add(stringTokenizer.nextToken());
        }

        return tokenList;
    }

    public List<FundDetails> getFundsFromTokenList(String tokenizedFunds) {
        List<String> tokenizedFundsList = getTokensFromString(tokenizedFunds, COMMA_DELIMITER);
        List<FundDetails> fundDetailsList = new ArrayList<>();

        for (String payment : tokenizedFundsList) {
            String[] paymentTokens = payment.split(COLON_DELIMITER);

            try {
                FundDetails fundDetails = new FundDetails();
                fundDetails.setCurrency(paymentTokens[0]);
                fundDetails.setAmount(Double.valueOf(paymentTokens[1]));
                fundDetailsList.add(fundDetails);
            } catch (Exception e) {
                logger.severe("Invalid funds file!" + e.getMessage());
            }
        }

        return fundDetailsList;
    }
}
