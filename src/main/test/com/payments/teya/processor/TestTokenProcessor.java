package com.payments.teya.processor;

import com.payments.teya.exceptions.InvalidInputException;
import com.payments.teya.testData.TestDataProvider;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.payments.teya.util.Constants.COMMA_DELIMITER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class TestTokenProcessor {
    TokenProcessor tokenProcessor = new TokenProcessor();
    @Test
    public void testGetPaymentsList() throws InvalidInputException {
        List<String> paymentTokens = tokenProcessor.getPaymentsFromString(TestDataProvider.paymentsInputString());
        assertEquals(8, paymentTokens.size());
    }

    @Test
    public void testGetTokensFromString() throws InvalidInputException {
        List<String> tokens = tokenProcessor.getTokensFromString(TestDataProvider.paymentsInputString(), COMMA_DELIMITER);
        assertEquals(8, tokens.size());

    }

    @Test
    public void testGetFundsFromString() throws InvalidInputException {
        Map<String, Double> funds = tokenProcessor.getFundsFromString(TestDataProvider.fundsInputString());
        assertEquals(3, funds.size());
    }

    @Test
    public void testGetPaymentsEmptyList() {
        assertThrowsExactly(InvalidInputException.class, () -> tokenProcessor.getPaymentsFromString(""),
                "Invalid input received!");
    }

    @Test
    public void testGetTokensFromEmptyString() {
        assertThrowsExactly(InvalidInputException.class, () -> tokenProcessor.getTokensFromString("", COMMA_DELIMITER),
                "Invalid input received!");
    }

    @Test
    public void testGetTokensFromEmptyDelimiterString() {
        assertThrowsExactly(InvalidInputException.class, () -> tokenProcessor.getTokensFromString(TestDataProvider.paymentsInputString(),""),
        "Invalid input received!");
    }

    @Test
    public void testGetFundsFromEmptyString() {
        assertThrowsExactly(InvalidInputException.class, () -> tokenProcessor.getFundsFromString(""),
                "Invalid input received!");
    }
}
