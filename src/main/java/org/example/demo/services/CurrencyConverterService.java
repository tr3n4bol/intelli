package org.example.demo.services;

import org.example.demo.dao.ConversionOperationDao;
import org.example.demo.dao.ExchangeRateDao;
import org.example.demo.entities.ConversionOperation;
import org.example.demo.entities.ExchangeRate;

import java.util.List;

public class CurrencyConverterService {
    private final ConversionOperationDao operationDao = new ConversionOperationDao();
    private final ExchangeRateDao rateDao = new ExchangeRateDao();

    public double convertCurrency(String fromCurrency, String toCurrency, double amount, String username) {
        ExchangeRate rate = rateDao.findByFromAndToCurrency(fromCurrency, toCurrency)
                .orElseGet(() -> {
                    ExchangeRate reverseRate = rateDao.findByFromAndToCurrency(toCurrency, fromCurrency)
                            .orElseThrow(() -> new IllegalArgumentException("Exchange rate not found"));

                    ExchangeRate newRate = new ExchangeRate();
                    newRate.setFromCurrency(fromCurrency);
                    newRate.setToCurrency(toCurrency);
                    newRate.setRate(1.0 / reverseRate.getRate());
                    return newRate;
                });

        double convertedAmount = amount * rate.getRate();

        ConversionOperation operation = new ConversionOperation();
        operation.setUsername(username);
        operation.setFromCurrency(fromCurrency);
        operation.setToCurrency(toCurrency);
        operation.setAmount(amount);
        operation.setConvertedAmount(convertedAmount);
        operation.setTimestamp(java.time.LocalDateTime.now());

        operationDao.save(operation);

        return convertedAmount;
    }

    public List<ConversionOperation> getHistory(String username) {
        return operationDao.getHistory(username);
    }

    public List<ExchangeRate> getAllExchangeRates() {
        return rateDao.findAll();
    }
}