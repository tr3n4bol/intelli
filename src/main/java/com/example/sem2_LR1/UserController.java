package com.example.sem2_LR1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversionOperationRepository conversionOperationRepository;

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

        List<ConversionOperation> operations = conversionOperationRepository.findByUsernameOrderByTimestampDesc(username);
        model.addAttribute("operations", operations);
        model.addAttribute("username", username);
        model.addAttribute("source", source);

        return "user-history";
    }

    @PostMapping("/switch-role/{username}")
    public String switchUserRole(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        if (user.getRole().equals("USER")) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }

        userRepository.save(user);

        return "redirect:/users";
    }

}