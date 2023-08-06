package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.model.FundDetails;
import com.payments.teya.model.PaymentDetails;
import com.payments.teya.util.TokenProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FeeProcessor {
    Logger logger = AppLogger.getLogger("FeeProcessor");
    private static final String COLON_DELIMITER = ":";
    private static final String GBP_CURRENCY ="GBP";

    private Map<String, Double> fundDetailsMap;
    public List<PaymentDetails> getPaymentsAfterFees(List<String> paymentsList, String availableFunds) {


        TokenProcessor tokenProcessor = new TokenProcessor();
        setFundDetailsMap(tokenProcessor.getFundsFromTokenList(availableFunds));

        List<PaymentDetails> sortedObjectPaymentsList = getSortedObjectPaymentList(paymentsList);

        List<PaymentDetails> paymentListAvailableFunds = getPaymentsAfterFeesList(sortedObjectPaymentsList);


        return paymentListAvailableFunds;
    }

    private Double getAmountAfterFeeDeduction(Double amount, String currency) {
        // Processing fee 1/3rd of a percent for GBP else 1/2 of a percent for other currencies
        Double deductedAmount = amount - (amount/(currency.equalsIgnoreCase(GBP_CURRENCY)? 300 : 200));
        BigDecimal scaledAmount =  new BigDecimal(deductedAmount).setScale(2, RoundingMode.CEILING);
        return scaledAmount.doubleValue();
    }

    private boolean includePaymentInBatch(String currency, Double paymentAmount ) {
        Double fundsAvailableForCurrency = 0.0;
        try {

            if (getFundDetailsMap().size() > 0 && getFundDetailsMap().containsKey(currency)) {
                fundsAvailableForCurrency = getFundDetailsMap().get(currency);
            }

            Double fundsAvailableAfterPayment = fundsAvailableForCurrency - paymentAmount;

            if (fundsAvailableAfterPayment > 0) {
                Map<String, Double> updatedAvailableFunds = getFundDetailsMap();
                updatedAvailableFunds.put(currency, fundsAvailableAfterPayment);
                setFundDetailsMap(updatedAvailableFunds);
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
                paymentDetails.setAmount(getAmountAfterFeeDeduction(paymentAmount, paymentTokens[1]));
                paymentDetailsList.add(paymentDetails);
            } catch (Exception e) {
                logger.severe("Error processing payment for merchant Id::" + paymentTokens[0] + "with error::" + e.getMessage());
            }
        }

        // Sort the list so that smaller amounts are considered first to be added to the batch
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

    private Map<String, Double> getFundDetailsMap() {
        return fundDetailsMap;
    }

    private void setFundDetailsMap(Map<String, Double> fundDetailsMap) {
        this.fundDetailsMap = fundDetailsMap;
    }
}
