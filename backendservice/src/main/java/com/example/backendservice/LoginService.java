package com.example.backendservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.*;


@Service
public class LoginService {
    @Autowired
    LoginRepository loginRepository;

    @Autowired private EmailService emailService;

    public Login newLogin(Login login) {
        Optional<Login> Login = loginRepository.findById(login.getEmail());
        if (Login.isPresent()) {
            return null;
        } else {
            return loginRepository.save(login);
        }
    }

    public String findbyemail(Login userinput) {
        Optional<Login> Login = loginRepository.findById(userinput.getEmail());
        if (Login.isPresent()) {
            Login l1 = Login.get();
            if (l1.getPassword().equals(userinput.getPassword())) {
                if (l1.isLocked()) {
                    return "Your account has been locked";
                } else {
                    return "pass";
                }
            } else {
                return "invalid password";
            }

        } else {
            return "username doesn't exist";

        }


    }

    public String updatePassword(Login updatePass) {
        Optional<Login> login = loginRepository.findById(updatePass.getEmail());
        if (login.isPresent()) {
            Login l3 = login.get();
            l3.setPassword(updatePass.getPassword());
            loginRepository.save(l3);
            return "Password updated";
        } else {
            return "Username doesn't exist";


        }
    }

    public String generateOTP(String email) {
        Random rand = new Random();
        int max=9999, min=1000;
        Optional<Login> login = loginRepository.findById(email);
        if (login.isPresent()) {
            Login l1 = login.get();
            l1.setOtp(rand.nextInt(max - min + 1) + min);
            loginRepository.save(l1);
            EmailDetails details1 = new EmailDetails();
            details1.setRecipient("rahath.marisetti@gmail.com");
            details1.setMsgBody("This is your OTP:" + l1.getOtp() + "Please do not share with anyone."); // start from here in next class
            return "OTP generated successfully";

        } else {
            return "Invalid Email Address";
        }


    }
    public boolean validateOTP(Login login){
        String  l1  = login.getEmail();
        int l2 = login.getOtp();
        Optional<Login> login1 = loginRepository.findById(l1);
        if(login1.isPresent() && login1.get().getOtp() == l2) {
                return true;
            }
        return false;
        }
    }


}
