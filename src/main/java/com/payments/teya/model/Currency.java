package com.payments.teya.model;

public enum Currency {
    GBP ("GBP"),
    CZK ("CZK"),
    EUR("EUR");

    public final String label;

     Currency(String label) {
        this.label = label;
    }
}
