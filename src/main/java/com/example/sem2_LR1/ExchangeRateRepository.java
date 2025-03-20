package com.example.sem2_LR1;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    ExchangeRate findByFromCurrencyAndToCurrency(String fromCurrency, String toCurrency);
}