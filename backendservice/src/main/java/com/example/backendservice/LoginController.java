package com.example.backendservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


@RestController

public class LoginController {


    @Autowired
    LoginService loginService;

    @PostMapping("/newLogin")
    public Login newLogin (@RequestBody Login login){
        return loginService.newLogin(login);
    }

    @PostMapping("/validate")
    public String validatecreds (@RequestBody Login login){
        return loginService.findbyemail(login);
    }
    @PostMapping("/updatePassword")
    public String updatePass (@RequestBody Login login){
        return loginService.updatePassword(login);
    }

    @PostMapping("/generateOTP")
    public String updateOTP (@RequestBody String email){
        return loginService.generateOTP(email);
    }
    @PostMapping("/validateOTP")
    public boolean validateOTP (@RequestBody Login login){
        return loginService.validateOTP(login);
    }


}




