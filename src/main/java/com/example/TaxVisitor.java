package com.example;

//visitor
public class TaxVisitor {

    private static final double TAX_RATE = 0.10;
    private double taxAmount = 0;
    private double netProfit = 0;

    public void visit(Investment investment) {
        taxAmount = investment.getProfit() * TAX_RATE;
        netProfit = investment.getProfit() - taxAmount;
    }

    public double getTaxAmount() { return taxAmount; }
    public double getNetProfit() { return netProfit; }
}