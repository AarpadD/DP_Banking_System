package com.example.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    @Column(name = "acc_code")
    public String accCode;

    public double amount;
    public String type;   // RON, EUR
    public String level;  // BASIC, SILVER, GOLD

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id")
    public ClientEntity client;

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL)
    public List<TransferEntity> transfers = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    public List<InvestmentEntity> investments = new ArrayList<>();

    public AccountEntity() {}
}
