package com.payments.teya.model;

public class PaymentDetails implements Comparable<PaymentDetails>{
    private static final String COLON_DELIMITER = ":";
    private String merchantId;
    private String currency;
    private Double amount;

    @Override
    public int compareTo(PaymentDetails o) {
        return this.amount.compareTo(o.getAmount());
    }

    public String toString() {
        return this.getMerchantId() + COLON_DELIMITER
                + this.getCurrency() + COLON_DELIMITER + this.getAmount();
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
