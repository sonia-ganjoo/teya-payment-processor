package com.payments.teya.util;

import com.payments.teya.exceptions.ProcessingFeeException;
import com.payments.teya.model.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TestNumberUtil {
    @Test
    public void testFeeAmountWhenGBP() throws ProcessingFeeException {
        Double amountAfterFee = NumbersUtil.getAmountAfterFeeDeduction(Double.valueOf("25"), Currency.GBP.label);
        assertEquals(24.92, amountAfterFee);
    }

    @Test
    public void testFeeAmountWhenNonGBP() throws ProcessingFeeException {
        Double fee = NumbersUtil.getAmountAfterFeeDeduction(Double.valueOf("25"), Currency.CZK.label);
        assertEquals(24.88, fee);
    }

    @Test
    public void testFeeWhenInvalidCurrency() {
        assertThrowsExactly(ProcessingFeeException.class,
                () -> NumbersUtil.getAmountAfterFeeDeduction(Double.valueOf("25"), "RandomCurrency"),
                "Currency not supported!");
    }

    @Test
    public void testFeeWhenNegativeAmount() {
        assertThrowsExactly(ProcessingFeeException.class,
                () -> NumbersUtil.getAmountAfterFeeDeduction(Double.valueOf("-25"), Currency.GBP.label),
                "Invalid amount!");
    }

    @Test
    public void testFeeWhenEmptyCurrency() {
        assertThrowsExactly(ProcessingFeeException.class,
                () -> NumbersUtil.getAmountAfterFeeDeduction(Double.valueOf("25"), ""),
                "Currency not supported!");
    }

    @Test
    public void testValueReturnedUptoTwoDecimals() throws ProcessingFeeException {
        Double fee = NumbersUtil.getAmountAfterFeeDeduction(Double.valueOf("25"), Currency.CZK.label);
        assertEquals(24.88, fee);
    }
}
