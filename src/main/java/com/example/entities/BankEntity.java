package com.example.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank")
public class BankEntity {

    @Id
    @Column(name = "bank_code")
    public String bankCode;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL)
    public List<ClientEntity> clients = new ArrayList<>();

    public BankEntity() {}
    public BankEntity(String bankCode) { this.bankCode = bankCode; }
}
