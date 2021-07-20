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
    private ChoresRepository choresRepo;

    @Autowired
    private ScheduleRepository scheduleRepo;

    private Schedule schedule;
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
        List<User> userInfo = new ArrayList<>();
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
        for(Long id : userIds){
            for (User user : listUsers) {
                if (id.equals(user.getId())){
                    Optional<User> userInfomation = userRepo.findById(id);
                    userInfo.add((userInfomation.get()));
                }
            }
        }
        ArrayList<Chores> chores = (ArrayList<Chores>) choresRepo.findAll();
        int choreCount= 0;
        int day = 0;
        int i= 0;
        while (day!= 5){
            for (i = 0; i < 5; i++) {
                for (User user : userInfo) {
                    if (choreCount != 2 && i < 5){
                        System.out.println(user.getFullName() + " " + chores.get(i).getChore());
                        choreCount++;
                    }else{
                        choreCount=0;
                        i++;
                    }
                }
            }
            System.out.println("Day: "+ i);
            day++;
        }
        model.addAttribute("userInfo", userInfo);
        return "schedule";
    }
}