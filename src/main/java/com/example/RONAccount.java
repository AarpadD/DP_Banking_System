package com.example;

public class RONAccount extends Account{
    public RONAccount(String accountCode, double amount) {
        super(accountCode, amount, Account.TYPE.RON);
    }

    @Override
    public double getInterest() {
        if (amount < 500)
            return 0.03;
        else
            return 0.08;
    }
}
