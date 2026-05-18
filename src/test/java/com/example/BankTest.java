package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    @Test
    void testBank() {
        Bank bank = new Bank("Test bank");
        assertEquals("Test bank", bank.getBankCode());
    }

    @Test
    void testAddCl() {
        Bank bank = new Bank("Test bank");
        Client cl = new Client.Builder("Ana Maria", "Str. Lxxi nr. 32", Account.TYPE.RON, "RON123", 1000).build();
        bank.addClient(cl);
        assertEquals(cl, bank.getClient("Ana Maria"));
    }
}
