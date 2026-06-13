package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.addPhoneNumber.AddPhonRequest;
import com.sc.demo.model.dto.addPhoneNumber.CheckPhoneRequest;
import com.sc.demo.service.addPhone.AddPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class addPhoneController {

    @Autowired
    private AddPhoneService addPhoneService;

    // التحقق من وجود رقم الهاتف
    @GetMapping("/V1/api/sc/checkNumber")
    public CheckPhoneRequest checkNumber(@RequestParam long phone){
        return addPhoneService.checkForTheNumber(phone);
    }

    // اضافة رقم هاتف جديد
    @PostMapping("/V1/api/sc/AddNewPhone")
    public Boolean addNewPhone(@RequestBody AddPhonRequest addPhonRequest){
        return addPhoneService.addPhone(addPhonRequest);
    }
}
