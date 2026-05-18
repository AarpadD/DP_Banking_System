package com.example.repositories;

import com.example.entities.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepo extends JpaRepository<TransferEntity, String> {}