package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @Test
    void testClient() {
        Client cl = new Client.Builder("Ana Maria", "Str. Lxxi nr. 32", Account.TYPE.RON, "RON123", 1000).build();
        assertEquals("Ana Maria", cl.getName());
        assertEquals(1000, cl.getAccount("RON123").getAmount());

    }

    @Test
    void testAddAcc() {
        Client cl = new Client.Builder("Ana Maria", "Str. Lxxi nr. 32", Account.TYPE.RON, "RON123", 1000).build();
        cl.addAccount(Account.TYPE.EUR, "EUR456", 500);
        assertEquals(500, cl.getAccount("EUR456").getAmount());
    }

    @Test
    void testCloseAcc() {
        Client cl = new Client.Builder("Ana Maria", "Str. Lxxi nr. 32", Account.TYPE.RON, "RON123", 1000).build();
        cl.addAccount(Account.TYPE.EUR, "EUR456", 500);
        assertEquals(500, cl.getAccount("EUR456").getAmount());

        cl.closeAccount("RON123");
        assertNull(cl.getAccount("RON123"));
    }

    @Test
    void testSetName() {
        Client cl = new Client.Builder("Ana Maria", "Str. Lxxi nr. 32", Account.TYPE.RON, "RON123", 1000).build();
        cl.setName("Maria Ana");
        assertEquals("Maria Ana", cl.getName());
    }

    @Test
    void testBuilder() {
        Client cl = new Client.Builder("Ana Maria", "Str. Lxxi nr. 32", Account.TYPE.RON, "RON123", 1000).email("ana.maria@gmail.com").birthday("06.06.2004").build();
        assertEquals("Ana Maria", cl.getName());
        assertEquals("ana.maria@gmail.com", cl.getEmail());
        assertEquals("06.06.2004", cl.getBirthday());
        assertEquals("Str. Lxxi nr. 32", cl.getAddress());
        assertEquals(1000, cl.getAccount("RON123").getAmount());
    }

    @Test
    void testUpgradeToSilver() {
        Client cl = new Client.Builder("Ana Maria", "Str. Lxxi nr. 32", Account.TYPE.RON, "RON123", 1000).build();
        cl.upgradeAccount("RON123", "SILVER");
        AccountDecorator silver = (AccountDecorator) cl.getAccount("RON123");
        assertEquals(0.08 + 0.015, silver.getInterest());
        assertEquals("SILVER", silver.getLevel());

    }

    @Test
    void testUpgradeToGold() {
        Client cl = new Client.Builder("Ana Maria", "Str. Lxxi nr. 32", Account.TYPE.RON, "RON123", 1000).build();
        cl.upgradeAccount("RON123", "GOLD");
        AccountDecorator gold = (AccountDecorator) cl.getAccount("RON123");
        assertEquals(0.08 + 0.03, gold.getInterest());
    }

    @Test
    void testMakeInvestment() {
        Client cl = new Client.Builder("Ana Maria", "Str. Lxxi nr. 32", Account.TYPE.RON, "RON123", 1000).build();
        cl.upgradeAccount("RON123", "SILVER");
        AccountDecorator silver = (AccountDecorator) cl.getAccount("RON123");
        silver.makeInvestment("ETF S&P 500", 200);
        assertEquals(800, cl.getAccount("RON123").getAmount());
    }
}
