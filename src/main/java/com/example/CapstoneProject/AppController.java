package com.example.CapstoneProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.server.PathParam;
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

    @GetMapping(value = "")
    public String viewHomePage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<User> listUser = userService.getAllUsers();
        if (userDetails == null) {
            return "home";
        }
        User user = userDetails.getUser(userDetails.getUsername());
        List<Schedule> currentSchedule = (List<Schedule>) scheduleRepo.findAll();
        model.addAttribute("currentSchedule", currentSchedule);
        for (User list : listUser) {
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

    @GetMapping("/new-schedule")
    public String newSchedule() {
        return "new-schedule";
    }

    @GetMapping("/schedule")
    public String listUser(Model model, @RequestParam int week) {
        List<User> listUsers = (List<User>) userRepo.findAll();
        listUsers.removeIf(user -> !user.getRole().equals("User"));
        model.addAttribute("listUsers", listUsers);
        ArrayList<Long> userIds = new ArrayList<>();
        ArrayList<Long> copies = new ArrayList<>();
        List<User> userInfo = new ArrayList<>();
        ArrayList<Chores> chores = (ArrayList<Chores>) choresRepo.findAll();
        int day = 1;
        int count = 0;
        while (count != 50) {
            double number = (Math.random() * (listUsers.get(listUsers.size() - 1).getId() - listUsers.get(0).getId() + 1)) + listUsers.get(0).getId();
            if (Collections.frequency(userIds, (long) number) > 1) {
                copies.add((long) number);
            } else if (!copies.contains((long) number)) {
                userIds.add((long) number);
                count++;
            }
        }
        for (Long id : userIds) {
            for (User user : listUsers) {
                if (id.equals(user.getId())) {
                    Optional<User> userInfomation = userRepo.findById(id);
                    if (userInfomation.isPresent()) {
                        userInfo.add((userInfomation.get()));
                    }
                }
            }
        }

        int choreCount = 0;
        int i = 0;
        int j = 0;
        int k = 0;
        Schedule newSchedule = new Schedule();
        while (day != 6) {
            for (i = 0; i < chores.size(); i++) {
                for (j = k; j < userInfo.size(); j++) {
                    if (choreCount != 2 && i < chores.size()) {
                        newSchedule.setId(userInfo.get(k).getId());
                        newSchedule.setUser(userInfo.get(j).getFullName());
                        newSchedule.setUsername(userInfo.get(j).getUsername());
                        newSchedule.setChore(chores.get(i).getChore());
                        newSchedule.setWeek(week);
                        newSchedule.setDay(day);
                        newSchedule.setManager("Bryce");
                        newSchedule.setUser_checked(false);
                        newSchedule.setMan_checked(false);
                        scheduleRepo.save(newSchedule);
                        choreCount++;
                        k++;
                    } else if (choreCount == 2) {
                        choreCount = 0;
                        i++;
                        j--;
                    }
                }
            }
            day++;
        }
        List<Schedule> currentSchedule = (List<Schedule>) scheduleRepo.findByWeek(week);
        model.addAttribute("currentSchedule", currentSchedule);
        return "schedule";
    }

    @GetMapping("/chores")
    public String chores(Model model) {
        List<Chores> chores = (List<Chores>) choresRepo.findAll();
        model.addAttribute("chores", chores);
        return "chores";
    }
    @GetMapping("/choreslist")
    public String choreslist(Model model) {
        List<Chores> chores = (List<Chores>) choresRepo.findAll();
        model.addAttribute("chores", chores);
        return "choreslist";
    }
    @GetMapping("/chore/{id}")
    public ModelAndView editChore(@PathVariable(name ="id")Long id) {
        ModelAndView mav =  new ModelAndView("edit-chore");
        Optional<Chores> chore = choresRepo.findById(id);
        mav.addObject("chore", chore);
        return mav;
    }
    @PostMapping("/edit-chore")
    public String editChore(Chores chore){
        Optional<Chores> oldChore = choresRepo.findById(chore.getId());
        System.out.println(chore.getId());
        if (oldChore != null) {
            oldChore.get().setChore(chore.getChore());
            oldChore.get().setDescription(chore.getDescription());
            choresRepo.save(oldChore.get());
        }
        return "redirect:/choreslist";
    }
    @GetMapping("/user")
    public String userProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetail){
        List<Schedule> user = (List<Schedule>) scheduleRepo.findUserByUsername(userDetail.getUsername());
        model.addAttribute("user", user);
        return "user";
    }
    @GetMapping("/check-off/{id}")
    public String checkedOff(@PathVariable(name ="id")Long id){
        Optional<Schedule> chore = scheduleRepo.findChoreById(id);
        Schedule c = chore.get();
        if (!c.isUser_checked()){
            c.setUser_checked(true);
        }else if(c.isUser_checked()){
            c.setUser_checked(false);
        }

        scheduleRepo.save(c);
        return "redirect:/user";
    }
}