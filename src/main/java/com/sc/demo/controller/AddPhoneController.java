package com.sc.demo.controller;

import com.sc.demo.model.dto.AddPhone.CheckPhone;
import com.sc.demo.service.AddPhone.AddPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddPhoneController {

    @Autowired
    private AddPhoneService addPhoneService;

    // التحقق من وجود رقم الهاتف
    @GetMapping("/V1/sc/api/checkNumber")
    public CheckPhone checkNumber(@RequestParam long phone_Number){
        return addPhoneService.checkForTheNumber(phone_Number);
    }

//    // اضافة رقم هاتف جديد
//    @PostMapping("/V1/sc/api/AddNewPhone")
//    public String addNewPhone(){
//        return addPhoneService.addPhone();
//    }
}
