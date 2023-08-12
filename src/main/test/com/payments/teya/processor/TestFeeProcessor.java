package com.payments.teya.processor;

import com.payments.teya.model.PaymentDetails;
import com.payments.teya.testData.TestDataProvider;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestFeeProcessor {
    FeeProcessor feeProcessor = new FeeProcessor();
    @Test
    public void testGetPaymentsAfterFees() {
        List<PaymentDetails>  paymentDetailsList =
                feeProcessor.getPaymentsAfterFees(TestDataProvider.testPaymentsList());
        assertEquals(8, paymentDetailsList.size());
        assertNotNull(paymentDetailsList.get(0));
    }

    @Test
    public void testGetPaymentsAfterFeesMissingCurrency() {
        List<String> paymentList = TestDataProvider.testPaymentsList();
        paymentList.add("1234::23.0");

        List<PaymentDetails>  paymentDetailsList =
                feeProcessor.getPaymentsAfterFees(paymentList);
        assertEquals(8, paymentDetailsList.size());
        assertNotNull(paymentDetailsList.get(0));
    }

    @Test
    public void testGetPaymentsAfterFeesMissingAmount() {
        List<String> paymentList = TestDataProvider.testPaymentsList();
        paymentList.add("1234:INR");

        List<PaymentDetails>  paymentDetailsList =
                feeProcessor.getPaymentsAfterFees(paymentList);
        assertEquals(8, paymentDetailsList.size());
        assertNotNull(paymentDetailsList.get(0));
    }

    @Test
    public void testGetPaymentsAfterFeesMissingId() {
        List<String> paymentList = TestDataProvider.testPaymentsList();
        paymentList.add(":INR:23.0");

        List<PaymentDetails>  paymentDetailsList =
                feeProcessor.getPaymentsAfterFees(paymentList);
        assertEquals(8, paymentDetailsList.size());
        assertNotNull(paymentDetailsList.get(0));
    }

    @Test
    public void testGetPaymentsAfterFeesSorted() {
        List<PaymentDetails>  paymentDetailsList =
                feeProcessor.getPaymentsAfterFees(TestDataProvider.testPaymentsList());
        assertEquals(8, paymentDetailsList.size());
        assertEquals("743:EUR:5.74", paymentDetailsList.get(0).toString());
        assertEquals("23:CZK:885.77", paymentDetailsList.get(7).toString());
    }

    @Test
    public void testGetPaymentsAfterFeesEmptyInput() {
        List<String>  paymentList = new ArrayList<>();
        List<PaymentDetails>  paymentDetailsList =
                feeProcessor.getPaymentsAfterFees(paymentList);
        assertEquals(0, paymentDetailsList.size());
    }
}
