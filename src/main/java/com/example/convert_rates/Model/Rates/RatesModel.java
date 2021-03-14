package com.example.convert_rates.Model.Rates;

public class RatesModel {
    private String firstCode;
    private String secondCode;
    private String firstDescription;
    private String secondDescription;
    private double firstRate;
    private double secondRate;
    private double amount = 100;
    private double total;

    public RatesModel() {
    }

    public String getFirstCode() {
        return firstCode;
    }

    public String getSecondCode() {
        return secondCode;
    }

    public String getFirstDescription() {
        return firstDescription;
    }

    public String getSecondDescription() {
        return secondDescription;
    }

    public double getFirstRate() {
        return firstRate;
    }

    public double getSecondRate() {
        return secondRate;
    }

    public double getAmount() {
        return amount;
    }

    public String getTotal() {
        total = firstRate / secondRate;
        // Converting to string and removing the tail of numbers
        String sValue = (String) String.format("%.2f", (total * amount));
        return sValue;
    }

    public void setFirstCode(String firstCode) {
        this.firstCode = firstCode;
    }

    public void setSecondCode(String secondCode) {
        this.secondCode = secondCode;
    }

    public void setFirstDescription(String firstDescription) {
        this.firstDescription = firstDescription;
    }

    public void setSecondDescription(String secondDescription) {
        this.secondDescription = secondDescription;
    }

    public void setFirstRate(double firstRate) {
        this.firstRate = firstRate;
    }

    public void setSecondRate(double secondRate) {
        this.secondRate = secondRate;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}