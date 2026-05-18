package com.example.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "investment")
public class InvestmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String asset;
    public double investedAmount;
    public double profit;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "acc_code")
    public AccountEntity account;

    public InvestmentEntity() {}
}