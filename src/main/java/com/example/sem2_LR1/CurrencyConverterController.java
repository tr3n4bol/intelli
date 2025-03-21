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

        List<ConversionOperation> operations = conversionOperationRepository
                .findByUsernameOrderByTimestampDesc(username) // Сортируем по времени в порядке убывания
                .stream()
                .limit(4) // Ограничиваем до 4 записей
                .collect(Collectors.toList());

        model.addAttribute("operations", operations);
        model.addAttribute("username", username);

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

            // Если курс не найден, проверяем обратный курс
            if (exchangeRate == null) {
                ExchangeRate reverseRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(toCurrency, fromCurrency);
                if (reverseRate != null) {
                    // Вычисляем обратный курс
                    exchangeRate = new ExchangeRate();
                    exchangeRate.setFromCurrency(fromCurrency);
                    exchangeRate.setToCurrency(toCurrency);
                    exchangeRate.setRate(1.0 / reverseRate.getRate());
                } else {
                    // Если ни прямой, ни обратный курс не найден, возвращаем ошибку

                    // Получаем список операций для текущего пользователя
                    List<ConversionOperation> operations = conversionOperationRepository.findByUsernameOrderByTimestampDesc(username);
                    model.addAttribute("operations", operations);

                    // Получаем список валют
                    List<String> currencies = exchangeRateRepository.findAll().stream()
                            .flatMap(rate -> List.of(rate.getFromCurrency(), rate.getToCurrency()).stream())
                            .distinct()
                            .collect(Collectors.toList());
                    model.addAttribute("currencies", currencies);

                    model.addAttribute("error", "Exchange rate not found for the selected currencies.");
                    return "converter";
                }
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

            // Получаем имя текущего пользователя
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Получаем список операций для текущего пользователя
            List<ConversionOperation> operations = conversionOperationRepository.findByUsernameOrderByTimestampDesc(username);
            model.addAttribute("operations", operations);

            // Получаем список валют
            List<String> currencies = exchangeRateRepository.findAll().stream()
                    .flatMap(rate -> List.of(rate.getFromCurrency(), rate.getToCurrency()).stream())
                    .distinct()
                    .collect(Collectors.toList());
            model.addAttribute("currencies", currencies);

            model.addAttribute("error", "An error occurred during conversion.");
            return "converter";
        }
    }
}