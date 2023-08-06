package com.payments.teya.processor;

import com.payments.teya.config.AppLogger;
import com.payments.teya.model.FundDetails;
import com.payments.teya.model.PaymentDetails;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BatchProcessor {
    Logger logger = AppLogger.getLogger("BatchProcessor");
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public String getBatchPaymentResult(List<PaymentDetails> paymentDetailsList) {
        Map<String, List<PaymentDetails>> currencyBatches = paymentDetailsList.stream()
                .collect(Collectors.groupingBy(x -> x.getCurrency()));
        //sort HashMap
        TreeMap<String, List<PaymentDetails>> sortedCurrencyBatches = new TreeMap<>(currencyBatches);
    return convertMapToString(sortedCurrencyBatches);
    }

    private String convertMapToString(Map<String, List<PaymentDetails>> currencyBatches) {
        StringBuffer stringBuffer = new StringBuffer();

        for (Map.Entry<String, List<PaymentDetails>> entry : currencyBatches.entrySet()) {
            stringBuffer.append(entry.getKey())
                    .append(LINE_SEPARATOR)
                    .append(entry.getValue().stream().map(x -> x.toString())
                            .collect(Collectors.joining(LINE_SEPARATOR)))
                    .append(LINE_SEPARATOR);
        }

        System.out.println(stringBuffer);
        return stringBuffer.toString();
    }
}