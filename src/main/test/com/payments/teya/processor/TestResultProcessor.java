package com.payments.teya.processor;

import com.payments.teya.model.PaymentDetails;
import com.payments.teya.testData.TestDataProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.payments.teya.testData.TestDataProvider.expectedHashMapString;
import static com.payments.teya.testData.TestDataProvider.expectedSortedHashMapString;
import static com.payments.teya.testData.TestDataProvider.expectedSortedHashMapStringEmptyKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestResultProcessor {
    ResultProcessor resultProcessor = new ResultProcessor();

    @Test
    public void testGetBatchPaymentResult() {
        String batchString = resultProcessor.getBatchPaymentResult(TestDataProvider.testPaymentDetailsList());
        assertEquals(expectedSortedHashMapString(), batchString);
    }

    @Test
    public void testGetBatchPaymentResultEmptyKey() {
        List<PaymentDetails> paymentDetailsList = TestDataProvider.testPaymentDetailsList();
        paymentDetailsList.add(new PaymentDetails("1234", "", 20.00));

        String batchString = resultProcessor.getBatchPaymentResult(paymentDetailsList);
        assertEquals(expectedSortedHashMapStringEmptyKey(), batchString);
    }

    @Test
    public void testConvertMapToString() {
        String hashMapString = resultProcessor.convertMapToString(TestDataProvider.testBatchHashMap());
        assertEquals(expectedHashMapString(), hashMapString);
    }
}
