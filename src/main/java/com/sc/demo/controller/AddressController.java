package com.sc.demo.controller;

import com.sc.demo.model.dto.addresses.Branches;
import com.sc.demo.model.dto.addresses.Departments;
import com.sc.demo.service.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    // افرع المؤسسة
    @GetMapping("/V1/api/sc/getBranches")
    public List<Branches> getBranches(){
        return addressService.getBranches();
    }

    // اقسام المؤسسة
    @GetMapping("/V1/api/sc/getDepartments")
    public List<Departments> getDepartments(){
        return addressService.getDepartments();
    }
}
