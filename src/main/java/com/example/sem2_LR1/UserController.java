package com.example.sem2_LR1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversionOperationRepository conversionOperationRepository;

    @GetMapping("/view-users")
    public String viewUsers(Model model) {
        List<User> users = userRepository.findAll();
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
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !currentUsername.equals(username)) {
            return "redirect:/converter";
        }

        List<ConversionOperation> operations = conversionOperationRepository.findByUsernameOrderByTimestampDesc(username);
        model.addAttribute("operations", operations);
        model.addAttribute("username", username);
        model.addAttribute("source", source);

        return "user-history";
    }


}