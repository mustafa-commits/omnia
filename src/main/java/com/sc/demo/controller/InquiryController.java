package com.sc.demo.controller;

import com.sc.demo.model.dto.InquiryRequest;
import com.sc.demo.service.inquiry.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    // استعلام بأسم الوصي
    @GetMapping("/V1/api/sc/inquiryByName")
    public List<InquiryRequest> inquiryByName(@RequestParam String GuardianName){
        return inquiryService.inquiryByName(GuardianName);
    }
}
