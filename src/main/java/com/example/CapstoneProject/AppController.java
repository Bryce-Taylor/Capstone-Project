package com.example.CapstoneProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CustomUserDetailsService userService;

    @GetMapping(value="")
    public String viewHomePage(@AuthenticationPrincipal CustomUserDetails userDetails,Model model) {
        List<User> listUser = userService.getAllUsers();
        if (userDetails == null) {
            return "home";
        }
        User user = userDetails.getUser(userDetails.getUsername());

        for (User list:listUser) {
            if (user.getUsername().equals(list.getUsername()) &&
                    user.getRole().equals("User")) {

                return "home";
            }
            if (user.getUsername().equals(list.getUsername()) &&
                    user.getRole().equals("Admin")) {
                return "home_admin";
            }
            if (user.getUsername().equals(list.getUsername()) &&
                    user.getRole().equals("Manager")) {
                return "home_manager";
            }
        }
        return "home";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }
    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(user);
        return "redirect:/";
    }
    @GetMapping("/schedule")
    public String listUser(Model model) {
        List<User> listUsers = (List<User>) userRepo.findAll();
        listUsers.removeIf(user -> !user.getRole().equals("User"));
        model.addAttribute("listUsers", listUsers);
        ArrayList<Long> userIds = new ArrayList<>();
        ArrayList<Long> copies= new ArrayList<>();
        int count = 0;
        for (int i = count; i < 5; i++){
            for (int j = 0; j < 10; j++) {
                double number = (Math.random() * (41 - 11 + 1)) + 11;
                if (Collections.frequency(userIds,(long) number) > 1) {
                    copies.add((long) number);
                    j--;
                } else if (!copies.contains((long) number)) {
                    userIds.add((long) number);
                    count ++;
                }
            }
        }
        System.out.println(userIds);
        return "schedule";
    }
}