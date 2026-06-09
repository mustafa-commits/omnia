package com.sc.demo.controller;

import com.sc.demo.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    // افرع المؤسسة
    @GetMapping("/V1/api/sc/getBranches")
    public String getBranches(){
        return addressService.getBranches();
    }
}
