package com.example.CapstoneProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.UnsupportedEncodingException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ChoresRepository choresRepo;

    @Autowired
    private ScheduleRepository scheduleRepo;

    private Schedule schedule;
    @Autowired
    private CustomUserDetailsService userService;

    @GetMapping(value = "")
    public String viewHomePage(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute("schedule") Schedule schedule, Model model) throws MessagingException, UnsupportedEncodingException {
        List<User> listUser = userService.getAllUsers();
        if (userDetails == null) {
            return "home";
        }
        User user = userDetails.getUser(userDetails.getUsername());
        LocalDate today = LocalDate.now();
        List<Schedule> currSchedule = (List<Schedule>) scheduleRepo.findAll();
        if (today.isAfter(currSchedule.get(0).getEnd_date())) {
            managerScoreCards();
            employeeScoreCards();
            scheduleRepo.deleteAll();
            makeSchedule();
            List<Schedule> currentSchedule = (List<Schedule>) scheduleRepo.findAll();
            model.addAttribute("currentSchedule", currentSchedule);
            List<User> listUsers = (List<User>) userRepo.findAll();
            model.addAttribute("user", listUsers);
        } else if(currSchedule.size() == 0){
            makeSchedule();
            List<Schedule> currentSchedule = (List<Schedule>) scheduleRepo.findAll();
            model.addAttribute("currentSchedule", currentSchedule);
            List<User> listUsers = (List<User>) userRepo.findAll();
            model.addAttribute("user", listUsers);
        }else if((today.getDayOfYear()/7)% 2 == 0 && today.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
            managerScoreCards();
            employeeScoreCards();
            List<Schedule> currentSchedule = (List<Schedule>) scheduleRepo.findAll();
            model.addAttribute("currentSchedule", currentSchedule);
        } else{
            List<Schedule> currentSchedule = (List<Schedule>) scheduleRepo.findAll();
            model.addAttribute("currentSchedule", currentSchedule);

        }
        for (User list : listUser) {
            if (user.getUsername().equals(list.getUsername()) &&
                    user.getRole().equals("User")) {
                int week_number = currSchedule.get(1).getDay().getDayOfYear()/7;
                model.addAttribute("week_number", week_number);
                int week_two_number = currSchedule.get(51).getDay().getDayOfYear()/7;
                model.addAttribute("week_two_number", week_two_number);
                return "home";
            }
            if (user.getUsername().equals(list.getUsername()) &&
                    user.getRole().equals("Admin")) {
                int week_number = currSchedule.get(1).getDay().getDayOfYear()/7;
                model.addAttribute("week_number", week_number);
                int week_two_number = currSchedule.get(51).getDay().getDayOfYear()/7;
                model.addAttribute("week_two_number", week_two_number);
                for (int chore = 0; chore < currSchedule.size(); chore++) {
                    if (currSchedule.get(chore).getDay().isBefore(today)) {
                        if (!currSchedule.get(chore).isUser_checked() || !currSchedule.get(chore).isMan_checked()) {
                            if (!currSchedule.get(chore).isNotified()) {
//                                missingChoreEmail(currSchedule.get(chore).getUser_email());
//                                missingChoreEmailManager(currSchedule.get(chore).getMan_email(),currSchedule.get(chore).getUser());
                                currSchedule.get(chore).setMissed(true);
                                currSchedule.get(chore).setNotified(true);
                                scheduleRepo.save(currSchedule.get(chore));
                            }
                        } else if (currSchedule.get(chore).isUser_checked() && currSchedule.get(chore).isMan_checked()) {
                            currSchedule.get(chore).setCompleted(true);
                            scheduleRepo.save(currSchedule.get(chore));
                        } else if (currSchedule.get(chore).isUser_checked() && !currSchedule.get(chore).isMan_checked()) {
//                            missingChoreEmail(currSchedule.get(chore).getMan_email());
                        }
                    }
                }
                List<User> listOfEmployees = (List<User>) userRepo.findAll();
                listOfEmployees.removeIf(users -> !users.getRole().equals("User"));
                model.addAttribute("listEmployees", listOfEmployees);
                List<User> listOfManager = (List<User>) userRepo.findAll();
                listOfManager.removeIf(users -> !users.getRole().equals("Manager"));
                model.addAttribute("listManagers", listOfManager);

                return "home_admin";
            }
            if (user.getUsername().equals(list.getUsername()) &&
                    user.getRole().equals("Manager")) {
                int week_number = currSchedule.get(1).getDay().getDayOfYear()/7;
                model.addAttribute("week_number", week_number);
                int week_two_number = currSchedule.get(51).getDay().getDayOfYear()/7;
                model.addAttribute("week_two_number", week_two_number);
                List<User> listOfEmployees = (List<User>) userRepo.findAll();
                listOfEmployees.removeIf(users -> !users.getRole().equals("User"));
                model.addAttribute("listEmployees", listOfEmployees);
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

    public void sendEmail(String recipientEmail, User user, int weekOne, int weekTwo)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("nonreply@cms.com", "CMS");
        helper.setTo(recipientEmail);
        String name = user.getFullName();
        String subject = "New Schedule is up!";

        String content = "<p>Hello, " + name + "</p>"
                + "<p>The schedule for "+ weekOne+" - "+weekTwo+" now up!</p>"
                + "<p>Click the link below to login :</p>"
                + "<br>"
                + "<p>Ignore this email if you not an employee of CMS. "
                + "<p>Thank you, Management. ";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void missingChoreEmail(String recipientEmail)
            throws MessagingException, UnsupportedEncodingException {
        System.out.println(recipientEmail);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("nonreply@cms.com", "Chore Management System");
        helper.setTo(recipientEmail);

        String subject = "You missed a Chore";

        String content = "<p>Hello,</p>"
                + "<p>This email was written to inform you that you missed a chore yesterday!</p>"
                + "<p>Please make sure that you are doing chores according to schedule. :</p>"
                + "<p>Click the link below to see complete schedule :</p>"
                + "<br>"
                + "<p>Ignore this email if you not an employee of CMS. "
                + "<p>Thank you, Management. ";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void missingChoreEmailManager(String recipientEmail, String name)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("nonreply@cms.com", "Chore Management System");
        helper.setTo(recipientEmail);

        String subject = "You missed a Chore";

        String content = "<p>Hello,</p>"
                + "<p>This email was written to inform you that " + name + " missed a chore yesterday!</p>"
                + "<p>Please make sure that all employees are doing chores according to schedule. :</p>"
                + "<p>Click the link below to see complete schedule :</p>"
                + "<br>"
                + "<p>Ignore this email if you not an employee of CMS. "
                + "<p>Thank you, Management. ";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    public String makeSchedule() throws MessagingException, UnsupportedEncodingException {
        int week = 1;
        LocalDate today = LocalDate.now();
        LocalDate weekOne = today.plus(2, ChronoUnit.DAYS);
        LocalDate weekTwo = weekOne.plus(11, ChronoUnit.DAYS);
        int weekOneNumber = weekOne.getDayOfYear()/7;
        int weekTwoNumber = weekTwo.getDayOfYear()/7;
        List<User> listOfUsers = (List<User>) userRepo.findAll();
//        for (int employee = 0; employee < listOfUsers.size(); employee++) {
//            sendEmail(listOfUsers.get(employee).getEmail(), listOfUsers.get(employee), weekOneNumber, weekTwoNumber);
//        }
        while (week != 3) {
            List<User> listUsers = (List<User>) userRepo.findAll();
            listUsers.removeIf(user -> !user.getRole().equals("User"));
            List<User> listManager = (List<User>) userRepo.findAll();
            listManager.removeIf(user -> !user.getRole().equals("Manager"));
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
            int managerCount = 0;
            int manCounter = 0;
            int i = 0;
            int j = 0;
            int k = 0;
            Schedule newSchedule = new Schedule();
            while (day != 6) {
                for (i = 0; i < chores.size(); i++) {
                    for (j = k; j < userInfo.size(); j++) {
                        if (choreCount != 2 && i < chores.size()) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/uuuu");
                            LocalDate friday = LocalDate.now();
                            LocalDate monday = friday.plus(4, ChronoUnit.DAYS);
                            LocalDate nextFriday = friday.plus(11, ChronoUnit.DAYS);
                            newSchedule.setStart_date(friday);
                            newSchedule.setEnd_date(nextFriday);
                            newSchedule.setId(userInfo.get(j).getId());
                            newSchedule.setUser(userInfo.get(j).getFullName());
                            newSchedule.setUsername(userInfo.get(j).getUsername());
                            newSchedule.setUser_email(userInfo.get(j).getEmail());
                            newSchedule.setChore(chores.get(i).getChore());
                            if (manCounter == 1) {
                                manCounter = 0;
                                managerCount++;
                            } else if (managerCount == listManager.size()) {
                                managerCount = 0;
                                manCounter = 0;
                                newSchedule.setManager(listManager.get(managerCount).getFullName());
                                newSchedule.setMan_username(listManager.get(managerCount).getUsername());
                                newSchedule.setMan_email(listManager.get(managerCount).getEmail());
                                manCounter++;
                            } else {
                                newSchedule.setManager(listManager.get(managerCount).getFullName());
                                newSchedule.setMan_username(listManager.get(managerCount).getUsername());
                                newSchedule.setMan_email(listManager.get(managerCount).getEmail());
                                manCounter++;
                            }
                            newSchedule.setWeek(week);
                            if (week == 1 && day == 1) {
                                int week_number = monday.getDayOfYear()/7;
                                newSchedule.setDay(monday);
                            } else if (week == 1 && day == 2) {
                                LocalDate nextDay = monday.plus(1, ChronoUnit.DAYS);
                                newSchedule.setDay(nextDay);
                            } else if (week == 1 && day == 3) {
                                LocalDate nextDay = monday.plus(2, ChronoUnit.DAYS);
                                newSchedule.setDay(nextDay);
                            } else if (week == 1 && day == 4) {
                                LocalDate nextDay = monday.plus(3, ChronoUnit.DAYS);
                                newSchedule.setDay(nextDay);
                            } else if (week == 1 && day == 5) {
                                LocalDate nextDay = monday.plus(4, ChronoUnit.DAYS);
                                newSchedule.setDay(nextDay);
                            } else if (week == 2 && day == 1) {
                                LocalDate nextDay = monday.plus(7, ChronoUnit.DAYS);
                                newSchedule.setDay(nextDay);
                            } else if (week == 2 && day == 2) {
                                LocalDate nextDay = monday.plus(8, ChronoUnit.DAYS);
                                newSchedule.setDay(nextDay);
                            } else if (week == 2 && day == 3) {
                                LocalDate nextDay = monday.plus(9, ChronoUnit.DAYS);
                                newSchedule.setDay(nextDay);
                            } else if (week == 2 && day == 4) {
                                LocalDate nextDay = monday.plus(10, ChronoUnit.DAYS);
                                newSchedule.setDay(nextDay);
                            } else if (week == 2 && day == 5) {
                                LocalDate nextDay = monday.plus(11, ChronoUnit.DAYS);
                                newSchedule.setDay(nextDay);
                            }
                            newSchedule.setUser_checked(false);
                            newSchedule.setUser_checkoff_time(null);
                            newSchedule.setMan_checked(false);
                            newSchedule.setMan_checkoff_time(null);
                            newSchedule.setMissed(false);
                            newSchedule.setCompleted(false);
                            newSchedule.setNotified(false);
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
            week++;
        }
        List<Schedule> currentSchedule = (List<Schedule>) scheduleRepo.findAll();
        return "currentSchedule";
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
    public ModelAndView editChore(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit-chore");
        Optional<Chores> chore = choresRepo.findById(id);
        mav.addObject("chore", chore);
        return mav;
    }

    @PostMapping("/edit-chore")
    public String editChore(Chores chore, @PathParam(value = "id") Long id) {
        Optional<Chores> oldChore = choresRepo.findById(id);
        if (oldChore != null) {
            oldChore.get().setChore(chore.getChore());
            oldChore.get().setDescription(chore.getDescription());
            choresRepo.save(oldChore.get());
        }
        return "redirect:/choreslist";
    }

    @GetMapping("/employeeList")
    public String listEmployee(Model model) {
        List<Schedule> listEmployees = (List<Schedule>) scheduleRepo.findAll();
        model.addAttribute("listEmployees", listEmployees);

        return "employeeList";
    }


    @GetMapping("/edit-employee/{id}")
    public String editEmployeeSchedule(@RequestParam(name = "name") String name, @PathVariable(value = "id") Long id, Model model) {
        Optional<Schedule> oldEmployee = scheduleRepo.findById(id);
        List<User> listOfEmployees = (List<User>) userRepo.findAll();
        if (oldEmployee.isPresent()) {
            oldEmployee.get().setUser(name);
            for (User user : listOfEmployees) {
                if (name.equals(user.getFullName())) {
                    oldEmployee.get().setUsername(user.getUsername());
                    oldEmployee.get().setUser_email(user.getEmail());
                }
            }
            scheduleRepo.save(oldEmployee.get());
        }
        return "redirect:/";
    }

    @GetMapping("/edit-manager/{id}")
    public String editManagerSchedule(@RequestParam(name = "name") String name, @RequestParam(name = "day") String day, @RequestParam(name = "chore") String chore, @PathVariable(value = "id") Long id, Model model) {
        Optional<Schedule> oldEmployee = scheduleRepo.findById(id);
        List<User> listOfEmployees = (List<User>) userRepo.findAll();
        List<Schedule> listOfSchedules = (List<Schedule>) scheduleRepo.findAll();
        LocalDate localDay = LocalDate.parse(day);
        if (oldEmployee.isPresent()) {
            for (Schedule schedule : listOfSchedules) {
                if (chore.equals(schedule.getChore()) && localDay.equals(schedule.getDay())) {
                    schedule.setManager(name);
                }
            }

            for (User user : listOfEmployees) {
                if (name.equals(user.getFullName())) {
                    oldEmployee.get().setMan_username(user.getUsername());
                    oldEmployee.get().setMan_email(user.getEmail());
                }
            }
            scheduleRepo.save(oldEmployee.get());
        }
        return "redirect:/";
    }


    @GetMapping("/user")
    public String userProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetail) {
        List<Schedule> user = (List<Schedule>) scheduleRepo.findUserByUsername(userDetail.getUsername());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/manager")
    public String managerProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetail) {
        List<Schedule> manager = (List<Schedule>) scheduleRepo.findUserByman_username(userDetail.getUsername());
        model.addAttribute("manager", manager);
        return "manager";
    }

    @GetMapping("/check-off/{id}")
    public String checkedOff(@PathVariable(name = "id") Long id) {
        Optional<Schedule> chore = scheduleRepo.findChoreById(id);
        Schedule c = chore.get();
        if (!c.isUser_checked()) {
            c.setUser_checked(true);
            LocalDateTime time = LocalDateTime.now();
            c.setUser_checkoff_time(time);
        } else if (c.isUser_checked()) {
            c.setUser_checked(false);
            c.setUser_checkoff_time(null);
        }

        scheduleRepo.save(c);
        return "redirect:/user";
    }

    @GetMapping("/man/check-off/{id}")
    public String checkedOffManager(@PathVariable(name = "id") Long id) {
        Optional<Schedule> chore = scheduleRepo.findChoreById(id);
        Schedule c = chore.get();
        if (!c.isMan_checked()){
            c.setMan_checked(true);
            LocalDateTime time = LocalDateTime.now();
            c.setMan_checkoff_time(time);
        }else if(c.isMan_checked()){
            c.setMan_checked(false);
            c.setMan_checkoff_time(null);
        }
        scheduleRepo.save(c);
        return "redirect:/manager";
    }
    @GetMapping("/update-chore/{id}")
    public ModelAndView editChoreUser(@PathVariable(name ="id")Long id, Model model) {
        List<Schedule> currentSchedule = (List<Schedule>) scheduleRepo.findAll();
        model.addAttribute("currentSchedule", currentSchedule);
        ModelAndView mav =  new ModelAndView("update-chore");
        Optional<Schedule> chore = scheduleRepo.findById(id);
        mav.addObject("chore", chore);
        return mav;
    }
    @PostMapping("/chore_info")
    public String chore_info(Schedule newPerson){
        Optional<Schedule> oldPerson = scheduleRepo.findById(newPerson.getId());
        if (oldPerson != null) {
            oldPerson.get().setUser(newPerson.getUser());
            oldPerson.get().setUsername(newPerson.getUsername());
            scheduleRepo.save(oldPerson.get());
        }
        return "redirect:/";
    }

    public void employeeScoreCard(String recipientEmail,String name , double percent, double overall, int week)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("nonreply@cms.com", "Chore Management System");
        helper.setTo(recipientEmail);

        String subject = "Weekly Scorecard";

        String content = "<p>Hello,</p>"
                + "<p>The weekly percentage for "+ name+ " was "+ percent+"% for the week </p>"
                + "<p>The overall group performance "+ overall +"% </p>"
                + "<p>Thank you, Management. ";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void employeeScoreCards() throws MessagingException, UnsupportedEncodingException {
        List<User> listOfUsers = (List<User>) userRepo.findAll();
        listOfUsers.removeIf(user -> !user.getRole().equals("User"));
        LocalDate today = LocalDate.now();
        for (User listOfUser : listOfUsers) {
            int counterWeek1 = 0;
            int counterWeek2 = 0;
            int week1 = 0;
            int week2 = 0;
            List<Schedule> chore = (List<Schedule>) scheduleRepo.findUserByUsername(listOfUser.getUsername());
            for (Schedule value : chore) {
                if(value.getWeek() == 1){
                    if (value.isUser_checked()) {
                        counterWeek1 += 1;
                        week1++;
                    }else{
                        if(value.getWeek() == 1) {
                            week1++;
                        }
                    }
                }else{
                    if (value.isUser_checked() && value.getWeek() == 2) {
                        counterWeek2 += 1;
                        week2++;
                    }else{
                        if(value.getWeek() == 2) {
                            week2++;
                        }
                    }
                }
            }
            if((today.getDayOfYear()/7) % 2 == 0 && !(week1 == 0)){
                double percentageWeek1 = (counterWeek1 / (double) week1 * 100);
//                employeeScoreCard(listOfUser.getEmail(),listOfUser.getFullName(),percentageWeek1, overallPercentage(), week1);

            }else if((today.getDayOfYear()/7) % 2 == 1 && !(week2 == 0)){
                double percentageWeek2 = (counterWeek2 / (double) week2 * 100);
//                employeeScoreCard(listOfUser.getEmail(),listOfUser.getFullName(),percentageWeek2, overallPercentage(),week2);
            }
        }

    }

    public void managerScoreCard(String recipientEmail,String name , double percent, double overall)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("nonreply@cms.com", "Chore Management System");
        helper.setTo(recipientEmail);

        String subject = "Weekly Scorecard";

        String content = "<p>Hello,</p>"
                + "<p>The weekly percentage for "+ name+ " was "+ percent+"% this week</p>"
                + "<p>The overall group performance "+ overall +"% </p>"
                + "<p>Thank you, Management. ";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    public void managerScoreCards() throws MessagingException, UnsupportedEncodingException {
        List<User> listOfUsers = (List<User>) userRepo.findAll();
        listOfUsers.removeIf(user -> !user.getRole().equals("Manager"));
        LocalDate today = LocalDate.now();
        for (User listOfUser : listOfUsers) {
            int counterWeek1 = 0;
            int counterWeek2 = 0;
            int week1 = 0;
            int week2 = 0;
            List<Schedule> chore = (List<Schedule>) scheduleRepo.findUserByman_username(listOfUser.getUsername());
            for (Schedule value : chore) {
                if(value.getWeek() == 1){
                    if (value.isMan_checked()) {
                        counterWeek1 += 1;
                        week1++;
                    }else{
                        if(value.getWeek() == 1) {
                            week1++;
                        }
                    }
                }else{
                    if (value.isMan_checked() && value.getWeek() == 2) {
                        counterWeek2 += 1;
                        week2++;
                    }else{
                        if(value.getWeek() == 2) {
                            week2++;
                        }
                    }
                }

            }
            if((today.getDayOfYear()/7) % 2 == 0){
                double percentageWeek1 = (counterWeek1 / (double) week1 * 100);
                System.out.println("Manager:" + listOfUser.getFullName() + " Score:" + percentageWeek1);
//                managerScoreCard(listOfUser.getEmail(),listOfUser.getFullName(),percentageWeek1, overallPercentage());

            }else{
                double percentageWeek2 = (counterWeek2 / (double) week2 * 100);
                System.out.println("Manager:" + listOfUser.getFullName() + " Score:" + percentageWeek2);
//                managerScoreCard(listOfUser.getEmail(),listOfUser.getFullName(),percentageWeek2, overallPercentage());
            }
            System.out.println("[Week 1: "+week1+ "] [Week 2: "+week2+"]");
        }

    }

    public double overallPercentage(){
        int count = 0;
        List<Schedule> currSchedule = (List<Schedule>) scheduleRepo.findAll();
        for (int j = 0; j < currSchedule.size(); j++){
            if (currSchedule.get(j).isUser_checked()){
                count+=1;
            }
        }
        double percent = (count/(double) currSchedule.size()) * 100;
        return percent;
    }
}