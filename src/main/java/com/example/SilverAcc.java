package com.example;

public class SilverAcc extends AccountDecorator {

    public SilverAcc(Account account) {
        super(account);
        this.level = "SILVER";
    }


    @Override
    public double getInterest() {
        return decoratedAccount.getInterest() + 0.015; 
    }

    @Override
    public double getTotalAmount() {
        return decoratedAccount.getAmount() + decoratedAccount.getAmount() * getInterest();
    }


    
}
