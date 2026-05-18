package com.example.repositories;

import com.example.entities.InvestmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvestmentRepo extends JpaRepository<InvestmentEntity, Long> {
    List<InvestmentEntity> findByAccount_AccCode(String accCode);
}