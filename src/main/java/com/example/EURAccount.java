package com.example;

public class EURAccount extends Account{
    public EURAccount(String accountCode, double amount) {
        super(accountCode, amount, Account.TYPE.EUR);
    }

    @Override
    public double getInterest() {
        return 0.01;
    }
    
}
