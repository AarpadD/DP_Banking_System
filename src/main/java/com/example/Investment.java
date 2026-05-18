package com.example;

public class Investment {

    private String asset;
    private double investedAmount;
    private double profit;

    public Investment(String asset, double investedAmount, double profit) {
        this.asset = asset;
        this.investedAmount = investedAmount;
        this.profit = profit;
    }

    public void accept(TaxVisitor visitor) {
        visitor.visit(this);
    }

    public double getInvestedAmount() { return investedAmount; }
    public double getProfit()         { return profit; }
    public String getAsset()          { return asset; }
}