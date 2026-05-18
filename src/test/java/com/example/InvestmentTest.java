package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InvestmentTest {

    @Test
    void testMakeInvestment() {
        Account acc = Account.createAccount(Account.TYPE.RON, "RON123", 1000);
        AccountDecorator silver = new SilverAcc(acc);

        Investment inv = silver.makeInvestment("BT Clasic", 200);
        assertEquals(800, silver.getAmount()); 
        assertEquals("BT Clasic", inv.getAsset());
        assertEquals(200, inv.getInvestedAmount());
    }

    @Test
    void testRetrieveInvestment() {
        Account acc = Account.createAccount(Account.TYPE.RON, "RON123", 1000);
        AccountDecorator silver = new SilverAcc(acc);

        Investment inv = silver.makeInvestment("BT Clasic", 200);
        silver.retrieveInvestment(inv, 50);
        assertEquals(1045, silver.getAmount()); 
    }

    @Test
    void testTaxVisitor() {
        Investment inv = new Investment("BT Clasic", 200, 50);
        TaxVisitor tax = new TaxVisitor();
        inv.accept(tax);
        assertEquals(5, tax.getTaxAmount());
        assertEquals(45, tax.getNetProfit());
    }
}