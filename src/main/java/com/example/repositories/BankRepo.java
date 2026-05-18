package com.example.repositories;

import com.example.entities.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepo extends JpaRepository<BankEntity, String> {}