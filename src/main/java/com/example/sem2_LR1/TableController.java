package com.example.sem2_LR1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TableController {

    @Autowired
    private peopleRepository peopleRepo;

    @GetMapping("/view")
    String peoplelist(Model model) {
        List<People> person = peopleRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("person", person);
        return "viewtable";
    }

    @GetMapping("/add")
    public String addPerson(Model model) {
        model.addAttribute("people", new People());
        return "add_person";
    }

    @PostMapping("/add")
    public String addPerson(@ModelAttribute("people") People people) {
        peopleRepo.save(people);
        return "redirect:/view";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        People person = peopleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid person Id:" + id));
        model.addAttribute("person", person);
        return "update";
    }

    @PostMapping("/update")
    public String updatePerson(@ModelAttribute("person") People updatedPerson) {
        People existingPerson = peopleRepo.findById(updatedPerson.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid person Id:" + updatedPerson.getId()));

        if (updatedPerson.getFirstName() != null && !updatedPerson.getFirstName().isEmpty()) {
            existingPerson.setFirstName(updatedPerson.getFirstName());
        }
        if (updatedPerson.getLastName() != null && !updatedPerson.getLastName().isEmpty()) {
            existingPerson.setLastName(updatedPerson.getLastName());
        }
        if (updatedPerson.getAge() != 0) {
            existingPerson.setAge(updatedPerson.getAge());
        }
        if (updatedPerson.getEmail() != null && !updatedPerson.getEmail().isEmpty()) {
            existingPerson.setEmail(updatedPerson.getEmail());
        }

        peopleRepo.save(existingPerson);
        return "redirect:/view";
    }

    @GetMapping("/delete")
    public String deletePerson(Model model) {
        return "delete_person";
    }

    @PostMapping("/delete")
    public String deletePerson(@RequestParam Long id) {
        peopleRepo.deleteById(id);
        return "redirect:/view";
    }

}
