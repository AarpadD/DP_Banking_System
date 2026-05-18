package com.example.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "transfer")
public class TransferEntity {

    @Id
    @Column(name = "transfer_code")
    public String transferCode;

    public double amount;
    public LocalDateTime timestamp;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "source_acc")
    public AccountEntity sourceAccount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "destination_acc")
    public AccountEntity destinationAccount;

    public TransferEntity() {}
}