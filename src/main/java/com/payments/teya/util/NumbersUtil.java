package com.payments.teya.util;

import com.payments.teya.exceptions.ProcessingFeeException;
import com.payments.teya.model.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * Util class for commonly used operations in the application
 */
public class NumbersUtil {
    private NumbersUtil() {

    }
    /**
     * method to calculate processing fee based on the currency
     * @param amount amount for which fee is to be calculated
     * @param currencyVal calculate amount based on this currency
     * @return processing fee for the merchant payment
     * @throws ProcessingFeeException throw this exception for any error in calculation
     */
    public static Double getAmountAfterFeeDeduction(Double amount, String currencyVal) throws ProcessingFeeException {
        if (amount.isNaN() || amount.isInfinite() || amount < 0) {
            throw new ProcessingFeeException("Invalid amount!");
        }
        if (Arrays.stream(Currency.values()).noneMatch(x -> x.label.equalsIgnoreCase(currencyVal))) {
            throw new ProcessingFeeException("Currency not supported!");
        }

        try {
            // Processing fee 1/3rd of a percent for GBP else 1/2 of a percent for other currencies
            double deductedAmount = amount - (amount / (currencyVal.equalsIgnoreCase(Currency.GBP.label) ? 300 : 200));
            BigDecimal scaledAmount = new BigDecimal(deductedAmount).setScale(2, RoundingMode.CEILING);
            return scaledAmount.doubleValue();
        } catch (NumberFormatException ne) {
            throw new ProcessingFeeException("Error in calculating processing fee!");
        }
    }
}
