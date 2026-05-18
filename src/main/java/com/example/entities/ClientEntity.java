package com.example.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name, address, email, birthday;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bank_code")
    public BankEntity bank;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    public List<AccountEntity> accounts = new ArrayList<>();

    public ClientEntity() {}
}
