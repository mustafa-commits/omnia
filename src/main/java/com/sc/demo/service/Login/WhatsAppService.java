package com.sc.demo.service.Login;

import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    public boolean sendVerificationCode(long code){
        System.out.println(code);
        return true;
    }
}
