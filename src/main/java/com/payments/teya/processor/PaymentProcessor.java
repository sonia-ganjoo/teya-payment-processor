package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.exceptions.InvalidInputException;
import com.payments.teya.model.PaymentDetails;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.payments.teya.util.Constants.PIPE_DELIMITER;

public class PaymentProcessor {

    Logger logger = AppLogger.getLogger("PaymentProcessor");
    /**
     * This method is responsible to return the output batch string
     * @param fundsAndPayments input string
     * @return the result string
     */
    public String generateBatchPayments(String fundsAndPayments) throws InvalidInputException {
        TokenProcessor tokenProcessor = new TokenProcessor();
        FeeProcessor feeProcessor = new FeeProcessor();
        ResultProcessor resultProcessor = new ResultProcessor();
        BatchProcessor batchProcessor = new BatchProcessor();

        String result;

        // split input string into funds and payments
        List<String> tokensList = tokenProcessor.getTokensFromString(fundsAndPayments,
                PIPE_DELIMITER);

        if (tokensList.size() == 2) {
            // get funds string
            String availableFunds = tokensList.get(0);

            // get payments string
            String payments = tokensList.get(1);

            logger.info("Read funds from input:::" + availableFunds);
            logger.info("Read payments from input:::" + payments);

            // get list of multiple payment strings
            List<String> paymentsList = tokenProcessor.getPaymentsFromString(payments);

            // get Map of available funds with currency as key
            Map<String, Double> fundDetailsMap = tokenProcessor.getFundsFromString(availableFunds);

            // get list of payments after deducting processing fees
            List<PaymentDetails> paymentAfterFeesList = feeProcessor.getPaymentsAfterFees(paymentsList);

            //calculate processing fees and add to the
            List<PaymentDetails> batchPaymentsList = batchProcessor.getBatchPaymentsList(paymentAfterFeesList, fundDetailsMap);

            // resultant batch string
            result = resultProcessor.getBatchPaymentResult(batchPaymentsList);
            System.out.println(result);
        } else {
            throw new InvalidInputException("Invalid input received!");
        }

        return result;
    }
}
