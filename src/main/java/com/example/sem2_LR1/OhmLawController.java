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
public class OhmLawController {

    @Autowired
    private OhmCalculationRepository ohmCalculationRepository;

    @Autowired
    private UserRepository userRepository;

    public OhmLawController(OhmCalculationRepository ohmCalculationRepository,
                            UserRepository userRepository) {
        this.ohmCalculationRepository = ohmCalculationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/ohm-calculator")
    public String showCalculator(Model model) {
        // Очищаем поля формы
        model.addAttribute("voltage", "");
        model.addAttribute("current", "");
        model.addAttribute("resistance", "");

        // Получаем историю операций для текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<OhmCalculation> history = ohmCalculationRepository
                .findTop4ByUsernameOrderByTimestampDesc(username);

        model.addAttribute("history", history);

        return "ohm-calculator";
    }

    @PostMapping("/calculate-ohm")
    public String calculateOhmLaw(
            @RequestParam(required = false) Double voltage,
            @RequestParam(required = false) Double current,
            @RequestParam(required = false) Double resistance,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        int providedParams = 0;
        if (voltage != null) providedParams++;
        if (current != null) providedParams++;
        if (resistance != null) providedParams++;

        if (providedParams != 2) {
            model.addAttribute("error", "Пожалуйста, введите ровно два параметра для расчета");
            model.addAttribute("voltage", voltage);
            model.addAttribute("current", current);
            model.addAttribute("resistance", resistance);
            return "ohm-calculator";
        }

        OhmCalculation calculation = new OhmCalculation();
        calculation.setVoltage(voltage);
        calculation.setCurrent(current);
        calculation.setResistance(resistance);
        calculation.setTimestamp(LocalDateTime.now());
        calculation.setUsername(username);

        String result;
        if (voltage == null) {
            voltage = current * resistance;
            result = String.format("Напряжение (V) = %.2f В", voltage);
            calculation.setVoltage(voltage);
            calculation.setCalculationType("V");
        } else if (current == null) {
            current = voltage / resistance;
            result = String.format("Ток (I) = %.2f А", current);
            calculation.setCurrent(current);
            calculation.setCalculationType("I");
        } else {
            resistance = voltage / current;
            result = String.format("Сопротивление (R) = %.2f Ом", resistance);
            calculation.setResistance(resistance);
            calculation.setCalculationType("R");
        }

        ohmCalculationRepository.save(calculation);

        model.addAttribute("result", result);
        model.addAttribute("voltage", "");
        model.addAttribute("current", "");
        model.addAttribute("resistance", "");

        List<OhmCalculation> history = ohmCalculationRepository
                .findTop4ByUsernameOrderByTimestampDesc(username);

        model.addAttribute("history", history);
        return "ohm-calculator";
    }
}