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

import static com.payments.teya.util.Constants.COLON_DELIMITER;

/**
 * This class is to calculate processing fees and deduct from the amount
 * payable to the merchants.
 */
public class PaymentProcessor {
    Logger logger = AppLogger.getLogger("FeeProcessor");

    private Map<String, Double> fundDetailsMap;

    /**
     * This method processed the payments and deducts
     * processing fees from the merchant amounts
     * @param paymentsList
     * @param availableFunds
     * @return list of payments to be included in the batch
     */
    public List<PaymentDetails> getPaymentsAfterFees(List<String> paymentsList, String availableFunds) {
        TokenProcessor tokenProcessor = new TokenProcessor();
        // get available funds in a hashmap
        fundDetailsMap = tokenProcessor.getFundsFromTokenList(availableFunds);

        //get list of objects so that fees can be calculated and debited from the amount
        List<PaymentDetails> sortedObjectPaymentsList = getSortedObjectPaymentList(paymentsList);

        //calculate processing fees and add to the
        List<PaymentDetails> paymentListAvailableFunds = getBatchPaymentsList(sortedObjectPaymentsList);

        return paymentListAvailableFunds;
    }

    /**
     * This method converts payments string into objects list and sorts the list
     * @param paymentsList
     * @return list of {@link com.payments.teya.model.PaymentDetails} object
     */
    private List<PaymentDetails> getSortedObjectPaymentList(List<String> paymentsList) {
        List<PaymentDetails> paymentDetailsList = new ArrayList<>();

        for (String payment : paymentsList) {
            String[] paymentTokens = payment.split(COLON_DELIMITER);
            Double paymentAmount = Double.valueOf(paymentTokens[2]);

            try {
                // convert payment string to objects for traversing and calculation purpose
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

    /**
     * This method returns final list of payments that are included in the batch
     * @param paymentDetailsList
     * @return list of {@link com.payments.teya.model.PaymentDetails} object
     */
    private List<PaymentDetails> getBatchPaymentsList(List<PaymentDetails> paymentDetailsList) {
        List<PaymentDetails> paymentListAvailableFunds = new ArrayList<>();

        for (PaymentDetails pd : paymentDetailsList) {
            // include payments in the batch only if funds are available
            if (includePaymentInBatch(pd.getCurrency(), pd.getAmount())) {
                paymentListAvailableFunds.add(pd);
            }
        }
        return paymentListAvailableFunds;
    }

    /**
     * This method checks if a payment should be included in the batch
     * based on the availability of the funds in Teya accounts
     * @param currency
     * @param paymentAmount
     * @return boolean to indicate if a payment should be included
     */
    private boolean includePaymentInBatch(String currency, Double paymentAmount ) {
        Double fundsAvailableForCurrency = 0.0;
        try {
            if (fundDetailsMap.size() > 0 && fundDetailsMap.containsKey(currency)) {
                fundsAvailableForCurrency = fundDetailsMap.get(currency);
            }

            // calculate available funds in the account after payments are made for other merchants
            Double fundsAvailableAfterPayment = fundsAvailableForCurrency - paymentAmount;

            if (fundsAvailableAfterPayment > 0) {
                // update available funds after processing other payments
                Map<String, Double> updatedAvailableFunds = fundDetailsMap;
                updatedAvailableFunds.put(currency, fundsAvailableAfterPayment);
                fundDetailsMap = updatedAvailableFunds;
                return true;
            }
        } catch (Exception e) {
            logger.severe("Error decusting processing fees!!" +e.getMessage());
        }

        return false;
    }
}
