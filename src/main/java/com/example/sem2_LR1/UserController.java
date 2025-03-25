package com.example.sem2_LR1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ConversionOperationRepository conversionOperationRepository;

    @Autowired
    private OhmCalculationRepository OhmCalculationRepository;

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/users"; // Перенаправление обратно на страницу пользователей
    }

    @GetMapping("/view-users")
    public String viewUsers(Model model) {
        List<User> users = userRepository.findAllByOrderByUsernameAsc();
        model.addAttribute("users", users);
        return "view-users";
    }

    @GetMapping("/view-history/{username}")
    public String viewUserHistory(
            @PathVariable String username,
            @RequestParam(required = false, defaultValue = "converter") String source, // Параметр source
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        List<ConversionOperation> operations_curr = conversionOperationRepository.findByUsernameOrderByTimestampDesc(username);
        List<OhmCalculation> operations_ohm = OhmCalculationRepository.findByUsernameOrderByTimestampDesc(username);

        model.addAttribute("operations_curr", operations_curr);
        model.addAttribute("operations_ohm", operations_ohm);
        model.addAttribute("username", username);
        model.addAttribute("source", source);

        return "user-history";
    }

    @PostMapping("/switch-role/{username}")
    public String switchUserRole(@PathVariable String username, Authentication authentication) {
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));


        if (currentUsername.equals(username)) {
            return "redirect:/users";
        }
        if (user.getRole().equals("USER")) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }

        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("/add-exchange-rate")
    public String showAddExchangeRateForm(Model model) {

        model.addAttribute("exchangeRate", new ExchangeRate());
        return "add-exchange-rate";
    }

    @PostMapping("/add-exchange-rate")
    public String addExchangeRate(@ModelAttribute ExchangeRate exchangeRate, Model model) {
        if (exchangeRateRepository.findByFromCurrencyAndToCurrency(
                exchangeRate.getFromCurrency(),
                exchangeRate.getToCurrency()) != null) {
            model.addAttribute("error", "Exchange rate already exists");
            return "add-exchange-rate";
        }

        exchangeRateRepository.save(exchangeRate);
        return "redirect:/converter";
    }

}