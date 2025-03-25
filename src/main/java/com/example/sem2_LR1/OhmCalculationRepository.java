package com.example.sem2_LR1;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OhmCalculationRepository extends JpaRepository<OhmCalculation, Long> {
    List<OhmCalculation> findTop4ByUsernameOrderByTimestampDesc(String username);
    List<OhmCalculation> findByUsernameOrderByTimestampDesc(String username);
}