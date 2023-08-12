package com.payments.teya.testData;

import com.payments.teya.model.PaymentDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.payments.teya.util.Constants.LINE_SEPARATOR;

public class TestDataProvider {

    public static String inputString() {
        return "GBP:100,EUR:200,CZK:1000|743:EUR:5.76,44:CHF:899,932:GBP:32.10,90" +
                "9:CZK:223.26,23:CZK:890.22,902:GBP:58.23,89:EUR:104.25" +
                ",663:EUR:97.43,902:EUR:20.01";
    }

    public static String fundsInputString() {
        return "GBP:100,EUR:200,CZK:1000";
    }

    public static String paymentsInputString() {
        return "743:EUR:5.76,44:CHF:899,932:GBP:32.10" +
                "909:CZK:223.26,23:CZK:890.22,902:GBP:58.23,89:EUR:104.25" +
                ",663:EUR:97.43,902:EUR:20.01";
    }
    public static String nonSupportedCurrencyInputString() {
        return "GBP:100,EUR:200,CZK:1000|743:EUR:5.76,44:CHF:899,932:GBP:32.10,90" +
                "9:CZK:223.26,23:CZK:890.22,902:GBP:58.23,89:EUR:104.25" +
                ",663:EUR:97.43,902:EUR:20.01,101:CHF:36";
    }

    public static String missingFundsInputString() {
        return "|743:EUR:5.76,932:GBP:32.10," +
                "909:CZK:223.26,23:CZK:890.22,902:GBP:58.23,89:EUR:104.25," +
                "663:EUR:97.43,902:EUR:20.01";
    }

    public static String missingPaymentsInputString() {
        return "GBP:100,EUR:200,CZK:1000|";
    }

    public static String missingDelimiterInputString() {
        return "GBP:100,EUR:200,CZK:1000,663:EUR:97.43,902:EUR:20.01";
    }

    public static List<String> testPaymentsList() {
        List<String> paymentList = new ArrayList<>();

        paymentList.add("44:CHF:899");
        paymentList.add("932:GBP:32.10");
        paymentList.add("909:CZK:223.26");
        paymentList.add("743:EUR:5.76");
        paymentList.add("23:CZK:890.22");
        paymentList.add("902:GBP:58.23");
        paymentList.add("89:EUR:104.25");
        paymentList.add("663:EUR:97.43");
        paymentList.add("902:EUR:20.01");
        paymentList.add("101:CHF:36");

        return paymentList;
    }

    public static List<PaymentDetails> testPaymentDetailsList() {
        List<PaymentDetails> paymentDetailsList = new ArrayList<>();

        paymentDetailsList.add(new PaymentDetails("932","GBP", Double.valueOf("32.10")));
        paymentDetailsList.add(new PaymentDetails("909","CZK", Double.valueOf("223.26")));
        paymentDetailsList.add(new PaymentDetails("743", "EUR",Double.valueOf("5.76")));
        paymentDetailsList.add(new PaymentDetails("23","CZK",  Double.valueOf("890.22")));
        paymentDetailsList.add(new PaymentDetails("902","GBP", Double.valueOf("58.23")));
        paymentDetailsList.add(new PaymentDetails("89","EUR",  Double.valueOf("104.25")));
        paymentDetailsList.add(new PaymentDetails("663","EUR", Double.valueOf("97.43")));
        paymentDetailsList.add(new PaymentDetails("902","EUR", Double.valueOf("20.01")));

        return paymentDetailsList;
    }

    public static Map<String, List<PaymentDetails>> testBatchHashMap() {
        return testPaymentDetailsList().stream()
                .collect(Collectors.groupingBy(PaymentDetails::currency));
    }

    public static String expectedResultantString() {
        return "CZK" + LINE_SEPARATOR +
                "909:CZK:222.15" +  LINE_SEPARATOR +
                "EUR" +  LINE_SEPARATOR +
                "743:EUR:5.74" +  LINE_SEPARATOR +
                "902:EUR:19.91" +  LINE_SEPARATOR +
                "663:EUR:96.95" +  LINE_SEPARATOR +
                "GBP" +  LINE_SEPARATOR +
                "932:GBP:32.0" +  LINE_SEPARATOR +
                "902:GBP:58.04"  + LINE_SEPARATOR;
    }

    public static String expectedSortedHashMapString() {
        return "CZK" + LINE_SEPARATOR +
                "909:CZK:223.26" + LINE_SEPARATOR +
                "23:CZK:890.22" + LINE_SEPARATOR +
                "EUR" + LINE_SEPARATOR +
                "743:EUR:5.76" + LINE_SEPARATOR +
                "89:EUR:104.25" + LINE_SEPARATOR +
                "663:EUR:97.43" + LINE_SEPARATOR +
                "902:EUR:20.01" + LINE_SEPARATOR +
                "GBP" + LINE_SEPARATOR +
                "932:GBP:32.1" + LINE_SEPARATOR +
                "902:GBP:58.23" + LINE_SEPARATOR;
    }

    public static String expectedSortedHashMapStringEmptyKey() {
        return "" + LINE_SEPARATOR +
               "1234::20.0" + LINE_SEPARATOR +
                expectedSortedHashMapString();
    }

    public static String expectedHashMapString() {
        return "EUR" + LINE_SEPARATOR +
                "743:EUR:5.76" + LINE_SEPARATOR +
                "89:EUR:104.25" + LINE_SEPARATOR +
                "663:EUR:97.43" + LINE_SEPARATOR +
                "902:EUR:20.01" + LINE_SEPARATOR +
                "GBP" + LINE_SEPARATOR +
                "932:GBP:32.1" + LINE_SEPARATOR +
                "902:GBP:58.23" + LINE_SEPARATOR +
                "CZK" + LINE_SEPARATOR +
                "909:CZK:223.26" + LINE_SEPARATOR +
                "23:CZK:890.22" + LINE_SEPARATOR;
    }
}
