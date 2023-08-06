package com.payments.teya.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumbersUtil {
    private static final String GBP_CURRENCY ="GBP";

    public static Double getAmountAfterFeeDeduction(Double amount, String currency) {
        // Processing fee 1/3rd of a percent for GBP else 1/2 of a percent for other currencies
        Double deductedAmount = amount - (amount/(currency.equalsIgnoreCase(GBP_CURRENCY)? 300 : 200));
        BigDecimal scaledAmount =  new BigDecimal(deductedAmount).setScale(2, RoundingMode.CEILING);
        return scaledAmount.doubleValue();
    }
}
