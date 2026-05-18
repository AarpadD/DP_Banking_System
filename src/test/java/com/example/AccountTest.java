package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    void testDepose() {
        Account acc = Account.createAccount(Account.TYPE.RON, "RON123", 100);
        acc.depose(50);
        assertEquals(150, acc.getAmount());
    }

    @Test
    void testRetrieve() {
        Account acc = Account.createAccount(Account.TYPE.RON, "RON123", 100);
        acc.retrieve(30);
        assertEquals(70, acc.getAmount());
    }

    @Test
    void testInterest() {
        Account acc = Account.createAccount(Account.TYPE.RON, "RON123", 400);
        assertEquals(0.03, acc.getInterest());
        acc.depose(200);
        assertEquals(0.08, acc.getInterest());

        Account acc2 = Account.createAccount(Account.TYPE.EUR, "EUR123", 100);
        assertEquals(0.01, acc2.getInterest());
    }

    @Test
    void testTransferRON() {
        Account acc1 = Account.createAccount(Account.TYPE.RON, "RON123", 100);
        Account acc2 = Account.createAccount(Account.TYPE.RON, "RON456", 50);
        acc2.transfer(acc1, 30); // take from acc1 and depose in acc2
        assertEquals(70, acc1.getAmount());
        assertEquals(80, acc2.getAmount());
    }

    @Test
    void testTransferEUR() {
        Account acc1 = Account.createAccount(Account.TYPE.RON, "RON123", 100);
        Account acc2 = Account.createAccount(Account.TYPE.EUR, "EUR456", 50);
        acc2.transfer(acc1, 50);
        assertEquals(50, acc1.getAmount());
        assertEquals(60, acc2.getAmount());
        acc1.transfer(acc2, 5);
        assertEquals(55, acc2.getAmount());
        assertEquals(75, acc1.getAmount());
    }

    @Test
    void testInvalidOp() {
        Account acc = Account.createAccount(Account.TYPE.RON, "RON123", 100);
        assertThrows(IllegalArgumentException.class, () -> acc.depose(-10));
        assertThrows(IllegalArgumentException.class, () -> acc.retrieve(-20));
        assertThrows(IllegalArgumentException.class, () -> acc.retrieve(200));
    }

    @Test
    void testCancelTransfer(){
        Account acc1 = Account.createAccount(Account.TYPE.RON, "RON123", 100);
        Account acc2 = Account.createAccount(Account.TYPE.RON, "RON456", 50);

        String transferCode =acc2.transfer(acc1, 30); // take from acc1 and depose in acc2
        assertEquals(70, acc1.getAmount());
        assertEquals(80, acc2.getAmount());
        
        acc2.cancelTransfer(transferCode);
        assertEquals(100, acc1.getAmount());
        assertEquals(50, acc2.getAmount());
    }

}
