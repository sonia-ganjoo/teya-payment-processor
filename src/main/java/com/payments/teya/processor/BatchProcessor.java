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

    public String getBatchPaymentResult(List<PaymentDetails> paymentDetailsList, List<FundDetails> fundsList) {
        Map<String, List<PaymentDetails>> currencyBatches = paymentDetailsList.stream()
                .collect(Collectors.groupingBy(x -> x.getCurrency()));
        //sort HashMap
        TreeMap<String, List<PaymentDetails>> sortedCurrencyBatches = new TreeMap<>(currencyBatches);
    return convertMapToString(sortedCurrencyBatches, fundsList);
    }

    private String convertMapToString(Map<String, List<PaymentDetails>> currencyBatches,
                                      List<FundDetails> fundsList) {
        StringBuffer stringBuffer = new StringBuffer();
        Double fundsAvailableForCurrency =0.0;

        for (Map.Entry<String, List<PaymentDetails>> entry : currencyBatches.entrySet()) {
            Double totalCurrentAmountForCurrency = entry.getValue().stream()
                    .map(x -> x.getAmount())
                    .reduce((k,v) -> k + v).orElse(0.0);

            Optional<FundDetails> fundsForCurrency = fundsList.stream()
                    .filter(x -> x.getCurrency().equalsIgnoreCase(entry.getKey()))
                    .findAny();

            if (fundsForCurrency.isPresent()) {
                fundsAvailableForCurrency = fundsForCurrency.get().getAmount();
            } else {
                logger.severe("Funds not available for this currency:::" + entry.getKey());
            }

            if (fundsAvailableForCurrency - totalCurrentAmountForCurrency > 0) {
                stringBuffer.append(entry.getKey())
                        .append(LINE_SEPARATOR)
                        .append(entry.getValue().stream().map(x -> x.toString())
                                .collect(Collectors.joining(LINE_SEPARATOR)))
                        .append(LINE_SEPARATOR);
            }
        }

        System.out.println(stringBuffer);
        return stringBuffer.toString();
    }
}