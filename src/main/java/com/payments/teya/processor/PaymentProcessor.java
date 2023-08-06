package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.model.PaymentDetails;
import com.payments.teya.util.NumbersUtil;
import com.payments.teya.util.TokenProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PaymentProcessor {
    Logger logger = AppLogger.getLogger("FeeProcessor");
    private static final String COLON_DELIMITER = ":";


    private Map<String, Double> fundDetailsMap;

    public List<PaymentDetails> getPaymentsAfterFees(List<String> paymentsList, String availableFunds) {
        TokenProcessor tokenProcessor = new TokenProcessor();
        // get available funds in a hashmap
        fundDetailsMap = tokenProcessor.getFundsFromTokenList(availableFunds);

        //get list of objects so that fees can be calculated and debited from the amount
        List<PaymentDetails> sortedObjectPaymentsList = getSortedObjectPaymentList(paymentsList);

        //calculate processing fees and add to the
        List<PaymentDetails> paymentListAvailableFunds = getPaymentsAfterFeesList(sortedObjectPaymentsList);


        return paymentListAvailableFunds;
    }



    private boolean includePaymentInBatch(String currency, Double paymentAmount ) {
        Double fundsAvailableForCurrency = 0.0;
        try {
            if (fundDetailsMap.size() > 0 && fundDetailsMap.containsKey(currency)) {
                fundsAvailableForCurrency = fundDetailsMap.get(currency);
            }

            Double fundsAvailableAfterPayment = fundsAvailableForCurrency - paymentAmount;

            if (fundsAvailableAfterPayment > 0) {
                Map<String, Double> updatedAvailableFunds = fundDetailsMap;
                updatedAvailableFunds.put(currency, fundsAvailableAfterPayment);
                fundDetailsMap = updatedAvailableFunds;
                return true;
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }

        return false;
    }

    private List<PaymentDetails> getSortedObjectPaymentList(List<String> paymentsList) {
        List<PaymentDetails> paymentDetailsList = new ArrayList<>();

        for (String payment : paymentsList) {
            String[] paymentTokens = payment.split(COLON_DELIMITER);
            Double paymentAmount = Double.valueOf(paymentTokens[2]);

            try {
                PaymentDetails paymentDetails = new PaymentDetails();
                paymentDetails.setMerchantId(paymentTokens[0]);
                paymentDetails.setCurrency(paymentTokens[1]);
                paymentDetails.setAmount(NumbersUtil.getAmountAfterFeeDeduction(paymentAmount, paymentTokens[1]));
                paymentDetailsList.add(paymentDetails);
            } catch (Exception e) {
                logger.severe("Error processing payment for merchant Id::" + paymentTokens[0] + "with error::" + e.getMessage());
            }
        }

        // Sort the list so that smaller amounts are calculated first to be added to the batch
        Collections.sort(paymentDetailsList);

        return paymentDetailsList;
    }

    private List<PaymentDetails> getPaymentsAfterFeesList(List<PaymentDetails> paymentDetailsList) {
        List<PaymentDetails> paymentListAvailableFunds = new ArrayList<>();

        for (PaymentDetails pd : paymentDetailsList) {
            if (includePaymentInBatch(pd.getCurrency(), pd.getAmount())) {
                paymentListAvailableFunds.add(pd);
            }
        }
        return paymentListAvailableFunds;
    }
}
