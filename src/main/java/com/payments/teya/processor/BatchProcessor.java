package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.model.PaymentDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class BatchProcessor {
    Logger logger = AppLogger.getLogger("BatchProcessor");
    private Map<String, Double> fundDetailsMap;

    BatchProcessor() {}
    BatchProcessor(Map<String, Double> fundDetailsMap) {
        this.fundDetailsMap = fundDetailsMap;
    }

    /**
     * This method returns final list of payments that are included in the batch
     * @param paymentDetailsList list of PaymentDetails
     * @return list of {@link com.payments.teya.model.PaymentDetails} object
     */
    public List<PaymentDetails> getBatchPaymentsList(List<PaymentDetails> paymentDetailsList) {
        List<PaymentDetails> paymentListAvailableFunds = new ArrayList<>();

        for (PaymentDetails pd : paymentDetailsList) {
            // include payments in the batch only if funds are available
            if (includePaymentInBatch(pd.currency(), pd.amount())) {
                paymentListAvailableFunds.add(pd);
            } else {
                logger.info("Insufficient funds, not processed payment:::" + pd);
            }
        }
        return paymentListAvailableFunds;
    }

    /**
     * This method checks if a payment should be included in the batch
     * based on the availability of the funds in Teya accounts
     * @param currency currency for batch processing
     * @param paymentAmount amount to be paid to the merchants
     * @return boolean to indicate if a payment should be included
     */
    protected boolean includePaymentInBatch(String currency, Double paymentAmount ) {

        Double fundsAvailableForCurrency;
        try {
            if (!fundDetailsMap.isEmpty() && fundDetailsMap.containsKey(currency)) {
                fundsAvailableForCurrency = fundDetailsMap.get(currency);
            } else {
                return false;
            }

            // calculate available funds in the account after payments are made for other merchants
            double fundsAvailableAfterPayment = fundsAvailableForCurrency - paymentAmount;

            if (fundsAvailableAfterPayment > 0) {
                // update available funds after processing other payments
                fundDetailsMap.put(currency, fundsAvailableAfterPayment);
                return true;
            }
        } catch (Exception e) {
            logger.severe("Error in batch processing!!" +e.getMessage());
        }

        return false;
    }
}
