package com.example.sem2_LR1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CurrencyConverterController {

    @Autowired
    private ConversionOperationRepository conversionOperationRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @GetMapping("/converter")
    public String showConverter(Model model) {
        // Получаем имя текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Получаем историю операций для текущего пользователя
        model.addAttribute("operations", conversionOperationRepository.findByUsername(username));

        // Получаем уникальные валюты из таблицы exchange_rates
        List<String> currencies = exchangeRateRepository.findAll().stream()
                .flatMap(rate -> List.of(rate.getFromCurrency(), rate.getToCurrency()).stream())
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("currencies", currencies);
        return "converter";
    }

    @PostMapping("/convert")
    public String convertCurrency(
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency,
            @RequestParam double amount,
            Model model) {

        try {
            // Получаем имя текущего пользователя
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Получаем курс из базы данных
            ExchangeRate exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(fromCurrency, toCurrency);
            if (exchangeRate == null) {
                model.addAttribute("error", "Exchange rate not found for the selected currencies.");
                return "converter";
            }

            // Выполняем конвертацию
            double convertedAmount = amount * exchangeRate.getRate();

            // Сохраняем операцию в базу данных
            ConversionOperation operation = new ConversionOperation();
            operation.setUsername(username);
            operation.setFromCurrency(fromCurrency);
            operation.setToCurrency(toCurrency);
            operation.setAmount(amount);
            operation.setConvertedAmount(convertedAmount);
            operation.setTimestamp(LocalDateTime.now());
            conversionOperationRepository.save(operation);

            // Возвращаемся на страницу конвертера
            return "redirect:/converter";
        } catch (Exception e) {
            // Логируем ошибку
            e.printStackTrace();
            model.addAttribute("error", "An error occurred during conversion.");
            return "converter";
        }
    }
}