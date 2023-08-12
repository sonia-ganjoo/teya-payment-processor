package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.exceptions.ProcessingFeeException;
import com.payments.teya.model.PaymentDetails;
import com.payments.teya.util.NumbersUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static com.payments.teya.util.Constants.COLON_DELIMITER;

/**
 * This class is to calculate processing fees and deduct from the amount
 * payable to the merchants.
 */
public class FeeProcessor {
    Logger logger = AppLogger.getLogger("FeeProcessor");

    /**
     * This method processed the payments and deducts
     * processing fees from the merchant amounts
     * @param paymentsList list of payment strings
     * @return list of {@link com.payments.teya.model.PaymentDetails} object
     */
    public List<PaymentDetails> getPaymentsAfterFees(List<String> paymentsList) {
        List<PaymentDetails> paymentDetailsList = new ArrayList<>();

        for (String payment : paymentsList) {
            String[] paymentTokens = payment.split(COLON_DELIMITER);

            if (paymentTokens.length == 3) {
                try {
                    Double paymentAmount = Double.valueOf(paymentTokens[2]);

                    // convert payment string to objects for traversing and calculation purpose
                    PaymentDetails paymentDetails = new PaymentDetails(paymentTokens[0], paymentTokens[1],
                            NumbersUtil.getAmountAfterFeeDeduction(paymentAmount, paymentTokens[1]));
                    paymentDetailsList.add(paymentDetails);
                } catch (ProcessingFeeException pfe) {
                    logger.severe("Error processing payment for merchant Id::" + paymentTokens[0] + " with error::" + pfe.getMessage());
                }
            } else {
                logger.warning("Invalid Payment String!");
            }
        }

        // Sort the list so that smaller amounts are calculated first to be added to the batch
        Collections.sort(paymentDetailsList);

        return paymentDetailsList;
    }
}
