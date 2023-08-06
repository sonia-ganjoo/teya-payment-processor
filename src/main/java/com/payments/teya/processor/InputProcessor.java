package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.model.PaymentDetails;
import com.payments.teya.util.TokenProcessor;

import java.util.List;
import java.util.logging.Logger;

import static com.payments.teya.util.Constants.PIPE_DELIMITER;

/**
 * Class to process input string
 * and return the result
 */
public class InputProcessor {
    Logger logger = AppLogger.getLogger("InputProcessor");
    private TokenProcessor tokenProcessor;
    private PaymentProcessor feeProcessor;
    private ResultProcessor batchProcessor;

    /**
     * This method is responsible to return the output batch string
     * @param fundsAndPayments
     * @return the result string
     */
    public String generateBatchPayments(String fundsAndPayments) {
        String result = "";
        String availableFunds = "";
        String payments = "";

        // instantiated at method level because it is not requires at any other place in the class
        feeProcessor = new PaymentProcessor();
        tokenProcessor = new TokenProcessor();
        batchProcessor = new ResultProcessor();

        // split input string into funds and payments
        List<String> tokensList = tokenProcessor.getTokensFromString(fundsAndPayments,
                PIPE_DELIMITER);

        if (tokensList.size() == 2) {
            // get funds string
            availableFunds = tokensList.get(0);

            // get payments string
            payments = tokensList.get(1);

            System.out.println(availableFunds);
            logger.fine("Read funds from input:::" + availableFunds);
            System.out.println(payments);
            logger.fine("Read payments from input:::" + payments);

            // get list of multiple payment strings
            List<String> paymentsList = tokenProcessor.getPaymentsList(payments);

            // get list of payments after deducting processing fees
            List<PaymentDetails> paymentAfterFeesList = feeProcessor.getPaymentsAfterFees(paymentsList, availableFunds);

            // resultant batch string
            result = batchProcessor.getBatchPaymentResult(paymentAfterFeesList);

        } else {
            System.out.println("Invalid input string");
            logger.severe("Invalid input string");
        }

        return result;
    }
}
