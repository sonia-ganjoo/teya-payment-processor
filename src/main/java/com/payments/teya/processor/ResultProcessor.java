package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.model.PaymentDetails;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.payments.teya.util.Constants.LINE_SEPARATOR;

/**
 * This class converts the payment batch to a string
 */
public class ResultProcessor {
    Logger logger = AppLogger.getLogger("BatchProcessor");

    /**
     * Method to get result string and sort results based on currency
     * @param paymentDetailsList list of Payment details object
     * @return result string
     */
    public String getBatchPaymentResult(List<PaymentDetails> paymentDetailsList) {
        Map<String, List<PaymentDetails>> currencyBatches = paymentDetailsList.stream()
                .collect(Collectors.groupingBy(PaymentDetails::currency));
        //sort HashMap
        TreeMap<String, List<PaymentDetails>> sortedCurrencyBatches = new TreeMap<>(currencyBatches);
    return convertMapToString(sortedCurrencyBatches);
    }

    /**
     * Method to generate result in the required format
     * @param currencyBatches batches grouped by currency
     * @return batch string
     */
    protected String convertMapToString(Map<String, List<PaymentDetails>> currencyBatches) {
        StringBuffer stringBuffer = new StringBuffer();

        try {
            for (Map.Entry<String, List<PaymentDetails>> entry : currencyBatches.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append(LINE_SEPARATOR)
                        .append(entry.getValue().stream().map(PaymentDetails::toString)
                                .collect(Collectors.joining(LINE_SEPARATOR)))
                        .append(LINE_SEPARATOR);
            }
        } catch (Exception e) {
            logger.severe("Error creating batch result string!" + e.getMessage());
        }
        return stringBuffer.toString();
    }
}