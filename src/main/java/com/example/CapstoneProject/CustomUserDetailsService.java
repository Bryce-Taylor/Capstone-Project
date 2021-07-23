package com.example.CapstoneProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    private User user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }

    public List<User> getAllUsers(){
        return (List<User>) userRepo.findAll();
    }

    public void updateResetPasswordToken(String resetToken, String email) throws CustomerNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            user.setReset_password_token(resetToken);
            userRepo.save(user);
        } else {
            throw new CustomerNotFoundException("Could not find any customer with the email " + email);
        }
    }

    public User getByResetPasswordToken(String resetToken) {
        return userRepo.findByPasswordResetToken(resetToken);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setReset_password_token(" ");
        userRepo.save(user);
    }


}
