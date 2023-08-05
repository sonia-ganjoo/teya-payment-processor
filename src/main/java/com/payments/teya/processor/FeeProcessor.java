package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.model.PaymentDetails;
import com.payments.teya.util.TokenProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Logger;

public class FeeProcessor {
    private static final String COLON_DELIMITER = ":";
    private static final String GBP_CURRENCY ="GBP";
    Logger logger = AppLogger.getLogger("FeeProcessor");
    public List<PaymentDetails> getPaymentsAfterFees(List<String> paymentsList) {
        List<PaymentDetails> paymentDetailsList = new ArrayList<>();

        for (String payment : paymentsList) {
            String[] paymentTokens = payment.split(COLON_DELIMITER);

            try {
                PaymentDetails paymentDetails = new PaymentDetails();
                paymentDetails.setMerchantId(paymentTokens[0]);
                paymentDetails.setCurrency(paymentTokens[1]);
                paymentDetails.setAmount(getPaymentsAfterFeeDeduction(Double.valueOf(paymentTokens[2]), paymentTokens[1]));
                paymentDetailsList.add(paymentDetails);
            } catch (Exception e) {
                logger.severe("Invalid payments file!" + e.getMessage());
            }
        }

        Collections.sort(paymentDetailsList);
        return paymentDetailsList;
    }

    private Double getPaymentsAfterFeeDeduction(Double amount, String currency) {
        // Processing fee 1/3rd of a percent for GBP else 1/2 of a percent for other currencies
        Double deductedAmount = amount - (amount/(currency.equalsIgnoreCase(GBP_CURRENCY)? 300 : 200));
        BigDecimal scaledAmount =  new BigDecimal(deductedAmount).setScale(2, RoundingMode.CEILING);
        return scaledAmount.doubleValue();
    }
}
