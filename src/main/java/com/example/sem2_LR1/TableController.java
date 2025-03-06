package com.example.sem2_LR1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TableController {

    @Autowired
    private peopleRepository peopleRepo;

    @GetMapping("/")
    String peoplelist(Model model) {
        List<People> person = peopleRepo.findAll();
        model.addAttribute("person", person);
        return "index";
    }

    /*
    @PostMapping("/add")
    public String addPerson(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam int age,
            @RequestParam String email) {

        People newPerson = new People();
        newPerson.setFirstName(firstName);
        newPerson.setLastName(lastName);
        newPerson.setAge(age);
        newPerson.setEmail(email);

        peopleRepo.save(newPerson);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deletePerson(@RequestParam long id) {
        peopleRepo.deleteById(id);
        return "redirect:/";
    }
     */

    @GetMapping("/add")
    public String addPerson(Model model) {
        model.addAttribute("people", new People());
        return "add_person";
    }

    @PostMapping("/add")
    public String addPerson(@ModelAttribute("people") People people) {
        peopleRepo.save(people);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deletePerson(Model model) {
        return "delete_person";
    }

    @PostMapping("/delete")
    public String deletePerson(@RequestParam Long id) {
        peopleRepo.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editPersonForm(@PathVariable Long id, Model model) {
        People person = peopleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid person Id:" + id));
        model.addAttribute("person", person);
        return "edit_person";
    }

    @PostMapping("/edit/{id}")
    public String editPerson(@PathVariable Long id, @ModelAttribute("person") People updatedPerson) {
        // Находим существующую запись по ID
        People existingPerson = peopleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid person Id:" + id));

        // Обновляем поля существующей записи
        existingPerson.setFirstName(updatedPerson.getFirstName());
        existingPerson.setLastName(updatedPerson.getLastName());
        existingPerson.setAge(updatedPerson.getAge());
        existingPerson.setEmail(updatedPerson.getEmail());

        // Сохраняем обновленную запись
        peopleRepo.save(existingPerson);
        return "redirect:/";
    }

}
