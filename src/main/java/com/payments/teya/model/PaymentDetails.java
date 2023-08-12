package com.payments.teya.model;

import static com.payments.teya.util.Constants.COLON_DELIMITER;

public record PaymentDetails(String merchantId,
        String currency,
        Double amount) implements Comparable<PaymentDetails>{


    @Override
    public int compareTo(PaymentDetails o) {
        return this.amount.compareTo(o.amount());
    }

    @Override
    public String toString() {
        return this.merchantId() + COLON_DELIMITER
                + this.currency() + COLON_DELIMITER + this.amount();
    }
}
