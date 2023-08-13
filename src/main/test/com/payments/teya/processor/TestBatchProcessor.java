package com.payments.teya.processor;

import com.payments.teya.model.Currency;
import com.payments.teya.model.PaymentDetails;
import com.payments.teya.testData.TestDataProvider;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestBatchProcessor {
    BatchProcessor batchProcessor = new BatchProcessor(TestDataProvider.testFundsHashMap());

    @Test
    public void testGetBatchPaymentsList() {
        List<PaymentDetails>  paymentAvailableList = batchProcessor
                .getBatchPaymentsList(TestDataProvider.testPaymentDetailsList());
        assertEquals(6, paymentAvailableList.size());
    }

    @Test
    public void testGetBatchPaymentsListEmptyPayments() {
        List<PaymentDetails>  paymentAvailableList = batchProcessor
                .getBatchPaymentsList(new ArrayList<>());
        assertEquals(0, paymentAvailableList.size());
    }

    @Test
    public void testGetBatchPaymentsListEmptyFunds() {
        batchProcessor = new BatchProcessor(new HashMap<>());
        List<PaymentDetails>  paymentAvailableList = batchProcessor
                .getBatchPaymentsList(TestDataProvider.testPaymentDetailsList());
        assertEquals(0, paymentAvailableList.size());
    }

    @Test
    public void testIncludePaymentInBatch() {
        assertTrue(batchProcessor.includePaymentInBatch(Currency.GBP.label, 25.55));
    }

    @Test
    public void testIncludePaymentInBatchInsufficientFunds() {
        assertFalse(batchProcessor.includePaymentInBatch(Currency.EUR.label, 220.00));
    }

    @Test
    public void testIncludePaymentInBatchInvalidCurrency() {
        assertFalse(batchProcessor.includePaymentInBatch("random", 23.45));
    }

    @Test
    public void testIncludePaymentInBatchEmptyFunds() {
        batchProcessor = new BatchProcessor();
        assertFalse(batchProcessor.includePaymentInBatch(Currency.CZK.label, 0.0));
    }
}
