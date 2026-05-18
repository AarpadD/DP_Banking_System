package com.example;

//decorator
public abstract class AccountDecorator extends Account {
    protected Account decoratedAccount;

    public AccountDecorator(Account account) {
        super(account.accountCode, 0, account.type);
        this.amount = account.amount;
        this.decoratedAccount = account;
    }

    
    @Override
    public double getTotalAmount() {
        return decoratedAccount.getTotalAmount();
    }

    @Override
    public void depose(double amount) {
        if (decoratedAccount == null) return;
        decoratedAccount.depose(amount);
        this.amount = decoratedAccount.getAmount();
    }

    @Override
    public void retrieve(double amount) {
        decoratedAccount.retrieve(amount);
        this.amount = decoratedAccount.getAmount();
    }

    @Override
    public double getInterest() {
        return decoratedAccount.getInterest();
    }

    @Override
    public String transfer(Account toAccount, double amount) {
        String code = decoratedAccount.transfer(toAccount, amount);
        this.amount = decoratedAccount.getAmount();
        return code;
    }

    public Investment makeInvestment(String asset, double sum) {
        decoratedAccount.retrieve(sum);
        this.amount = decoratedAccount.getAmount();
        return new Investment(asset, sum, 0);
    }

    public void retrieveInvestment(Investment investment, double profit) {
        Investment inv = new Investment(investment.getAsset(), investment.getInvestedAmount(), profit);
        TaxVisitor taxVisitor = new TaxVisitor();
        
        inv.accept(taxVisitor);
        double netAmount = inv.getInvestedAmount() + taxVisitor.getNetProfit();
        decoratedAccount.depose(netAmount);
        this.amount = decoratedAccount.getAmount();
    }

}
