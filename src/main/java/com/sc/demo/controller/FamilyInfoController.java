package com.sc.demo.controller;

import com.sc.demo.model.dto.FamilyInfo.ChildrenAndMailyFamilyMambersResponse;
import com.sc.demo.model.dto.FamilyInfo.FamilyInfoBasicResponse;
import com.sc.demo.model.dto.FamilyInfo.FamilyInfoHousingResponse;
import com.sc.demo.service.users.FamilyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class FamilyInfoController {

    @Autowired
    private FamilyInfoService familyInfoService;

    // جلب البيانات الاساسية للعائلة مثل ال(اسم, عمر, رقم هاتف, الخ .....)
    @GetMapping("/V1/sc/api/getFamilyBasicInformation")
    public List<FamilyInfoBasicResponse> getFamilyBasicInformation(String token){
        return familyInfoService.getFamilyBasicInfo(token);
    }

    // جلب بيانات سكن العائلة
    @GetMapping("/V1/sc/api/getFamilyHousingInformation")
    public List<FamilyInfoHousingResponse> getFamilyHousingInformation(@RequestParam String P_FAMILY_NO){
        return familyInfoService.getFamilyHousingInfo(P_FAMILY_NO);
    }

    // عدد افراد العائلة + عدد الايتام
    @GetMapping("/V1/sc/api/getChildrenAndMailyFamilyMambersResponse")
    public List<ChildrenAndMailyFamilyMambersResponse> getChildrenAndMailyFamilyMambersResponse(@RequestParam String P_FAMILY_NO){
        return familyInfoService.getChildrenAndMailyFamilyMambersResponse(P_FAMILY_NO);
    }
}
