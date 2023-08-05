package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.model.FundDetails;
import com.payments.teya.model.PaymentDetails;
import com.payments.teya.util.TokenProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class InputProcessor {
    Logger logger = AppLogger.getLogger("InputProcessor");
    private static final String PIPE_DELIMITER = "|";

    private TokenProcessor tokenProcessor;
    private FeeProcessor feeProcessor;

    private BatchProcessor batchProcessor;
    public String generateBatchPayments(String fundsAndPayments) {
        String result = "";
        String funds = "";
        String payments = "";
        feeProcessor = new FeeProcessor();
        tokenProcessor = new TokenProcessor();
        batchProcessor = new BatchProcessor();

        List<String> tokensList = tokenProcessor.getTokensFromString(fundsAndPayments, PIPE_DELIMITER);


        if (tokensList.size() == 2) {
            funds = tokensList.get(0);
            payments = tokensList.get(1);

            System.out.println(funds);
            logger.fine("Read funds from input:::" + funds);
            System.out.println(payments);
            logger.fine("Read payments from input:::" + payments);

            List<String> paymentsList = tokenProcessor.getPaymentsList(payments);

            List<PaymentDetails> paymentAfterFeesList = feeProcessor.getPaymentsAfterFees(paymentsList);
            List<FundDetails> fundDetailsList = tokenProcessor.getFundsFromTokenList(funds);

            result = batchProcessor.getBatchPaymentResult(paymentAfterFeesList, fundDetailsList);

        } else {
            System.out.println("Invalid input string");
        }

        return result;
    }
}
