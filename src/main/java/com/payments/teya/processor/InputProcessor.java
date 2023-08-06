package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.model.PaymentDetails;
import com.payments.teya.util.TokenProcessor;

import java.util.*;
import java.util.logging.Logger;

public class InputProcessor {
    Logger logger = AppLogger.getLogger("InputProcessor");
    private static final String PIPE_DELIMITER = "|";

    private TokenProcessor tokenProcessor;
    private PaymentProcessor feeProcessor;

    private OutputProcessor batchProcessor;
    public String generateBatchPayments(String fundsAndPayments) {
        String result = "";
        String availableFunds = "";
        String payments = "";
        feeProcessor = new PaymentProcessor();
        tokenProcessor = new TokenProcessor();
        batchProcessor = new OutputProcessor();

        List<String> tokensList = tokenProcessor.getTokensFromString(fundsAndPayments, PIPE_DELIMITER);


        if (tokensList.size() == 2) {
            availableFunds = tokensList.get(0);
            payments = tokensList.get(1);

            System.out.println(availableFunds);
            logger.fine("Read funds from input:::" + availableFunds);
            System.out.println(payments);
            logger.fine("Read payments from input:::" + payments);

            List<String> paymentsList = tokenProcessor.getPaymentsList(payments);

            List<PaymentDetails> paymentAfterFeesList = feeProcessor.getPaymentsAfterFees(paymentsList, availableFunds);

            result = batchProcessor.getBatchPaymentResult(paymentAfterFeesList);

        } else {
            System.out.println("Invalid input string");
        }

        return result;
    }
}
