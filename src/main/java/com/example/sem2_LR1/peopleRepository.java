package com.example.sem2_LR1;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface peopleRepository extends JpaRepository<People, Long> {
    List<People> findAll();
}