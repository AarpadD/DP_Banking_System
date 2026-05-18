package com.example;

public class GoldAcc extends AccountDecorator {
    
    public GoldAcc(Account account){
        super(account);
        this.level = "GOLD";
    }

    @Override
    public double getInterest() {
        return decoratedAccount.getInterest() + 0.03;
    }

    @Override
    public double getTotalAmount() {
        return decoratedAccount.getAmount() + decoratedAccount.getAmount() * getInterest();
    }

}
