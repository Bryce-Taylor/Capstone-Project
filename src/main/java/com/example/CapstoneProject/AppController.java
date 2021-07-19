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
        ArrayList userIds = new ArrayList();
        ArrayList copies= new ArrayList();
        int count = 0;
        for (int i = count; i < 5; i++){
            for (int j = 0; j < 10; j++) {
                int number = (int) (Math.random() * (41 - 11 + 1)) + 11;
                if (Collections.frequency(userIds, number) > 1) {
                    copies.add(number);
                    j--;
                } else if (!copies.contains(number)) {
                    userIds.add(number);
                    count ++;
                }
            }
        }
        System.out.println(copies);
        System.out.println(userIds);
        return "schedule";
    }
//    @GetMapping("/update_password/{id}")
//    public ModelAndView editOrder(@RequestParam Long id) {
//        ModelAndView mav =  new ModelAndView("edit_password");
//        Optional<User> user = userRepo.findById(id);
//        mav.addObject("user",user);
//        return mav;
//    }
//    @PostMapping("/update_success")
//    public String order_info(User newPassword){
//        Optional<User> oldUserPass = userRepo.findById(newPassword.getId());
//        if (oldUserPass.isPresent()) {
//            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            String encodedPassword = passwordEncoder.encode(newPassword.getPassword());
//            newPassword.setPassword(encodedPassword);
//            oldUserPass.get().setPassword(newPassword.getPassword());
//            userRepo.save(oldUserPass.get());
//        }
//        return "redirect:/home";
//    }
}