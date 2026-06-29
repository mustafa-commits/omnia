package com.sc.demo.service.login;

import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    public boolean sendVerificationCode(Long phone, String country_code, Long code){
        System.out.println(code);
        return true;
    }
}
