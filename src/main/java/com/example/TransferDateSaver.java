package com.example;

import java.time.LocalDateTime;
import java.util.UUID;

//command
public class TransferDateSaver {

    String transferCode;
    Account source;
    Account destination;
    double amount;
    LocalDateTime timestamp;

    public TransferDateSaver(Account source, Account destination, double amount) {
        this.transferCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public void undo() {
        if (LocalDateTime.now().isBefore(timestamp.plusMinutes(3))) {
            destination.transfer(source, amount);
        } else {
            throw new IllegalStateException("Timeframe of 3 minutes expired");
        }
    }

    public String getTransferCode() {
        return transferCode;
    }
}