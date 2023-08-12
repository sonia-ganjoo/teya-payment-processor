package com.payments.teya.processor;

import com.payments.teya.exceptions.InvalidInputException;
import com.payments.teya.testData.TestDataProvider;
import org.junit.jupiter.api.Test;

import static com.payments.teya.testData.TestDataProvider.expectedResultantString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TestPaymentProcessor {

    PaymentProcessor paymentProcessor = new PaymentProcessor();

    @Test
    public void testGenerateBatchPayments() throws InvalidInputException {
        String result = paymentProcessor.generateBatchPayments(TestDataProvider.inputString());
        assertEquals(expectedResultantString(), result);
    }

    @Test
    public void testGenerateBatchPaymentsNotSupportedCurrency() throws InvalidInputException {
        String result = paymentProcessor.generateBatchPayments(TestDataProvider.nonSupportedCurrencyInputString());
        assertEquals(expectedResultantString(), result);
    }

    @Test
    public void testGenerateBatchPaymentsMissingFunds() {
        assertThrowsExactly(InvalidInputException.class, () -> paymentProcessor.generateBatchPayments(TestDataProvider.missingFundsInputString()),
                "Invalid input received!");
    }

    @Test
    public void testGenerateBatchPaymentsMissingPayments() {
        assertThrowsExactly(InvalidInputException.class, () -> paymentProcessor.generateBatchPayments(TestDataProvider.missingPaymentsInputString()),
        "Invalid input received!");
    }

    @Test
    public void testGenerateBatchPaymentsMissingDelimiter() {
        assertThrowsExactly(InvalidInputException.class, () -> paymentProcessor.generateBatchPayments(TestDataProvider.missingDelimiterInputString()),
                "Invalid input received!");
    }
}
