package com.example.sem2_LR1;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConversionOperationRepository extends JpaRepository<ConversionOperation, Long> {
    List<ConversionOperation> findByUsername(String username);
}