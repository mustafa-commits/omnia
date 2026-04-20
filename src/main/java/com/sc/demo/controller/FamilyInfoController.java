package com.sc.demo.controller;

import com.sc.demo.model.dto.FamilyHealthStatusInfoResponse;
import com.sc.demo.model.dto.FamilyInfoBasicResponse;
import com.sc.demo.model.dto.FamilyInfoHousingResponse;
import com.sc.demo.service.users.FamilyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FamilyInfoController {

    @Autowired
    private FamilyInfoService familyInfoService;

    // جلب البيانات الاساسية للعائلة مثل ال(اسم, عمر, رقم هاتف, الخ .....)
    @GetMapping("/V1/sc/api/getFamilyBasicInformation")
    public FamilyInfoBasicResponse getFamilyBasicInformation(@RequestParam Long P_FAMILY_NO){
        return familyInfoService.getFamilyBasicInfo(P_FAMILY_NO);
    }

    // جلب بيانات سكن العائلة
    @GetMapping("/V1/sc/api/getFamilyHousingInformation")
    public FamilyInfoHousingResponse getFamilyHousingInformation(@RequestParam Long P_FAMILY_NO){
        return familyInfoService.getFamilyHousingInfo(P_FAMILY_NO);
    }

    @GetMapping("/V1/sc/api/getFamilyHealthStatusInfo")
    public FamilyHealthStatusInfoResponse getFamilyHealthStatusInfo(@RequestParam Long P_FAMILY_NO){
        return familyInfoService.getFamilyHealthStatusInfo(P_FAMILY_NO);
    }
}
