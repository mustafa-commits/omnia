package com.sc.demo.controller;

import com.sc.demo.model.dto.ChildrenAndMailyFamilyMambersResponse;
import com.sc.demo.model.dto.FamilyHealthStatusInfoResponse;
import com.sc.demo.model.dto.FamilyInfoBasicResponse;
import com.sc.demo.model.dto.FamilyInfoHousingResponse;
import com.sc.demo.service.users.FamilyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FamilyInfoController {

    @Autowired
    private FamilyInfoService familyInfoService;

    // جلب البيانات الاساسية للعائلة مثل ال(اسم, عمر, رقم هاتف, الخ .....)
    @GetMapping("/V1/sc/api/getFamilyBasicInformation")
    public List<FamilyInfoBasicResponse> getFamilyBasicInformation(@RequestParam String P_FAMILY_NO){
        return familyInfoService.getFamilyBasicInfo(P_FAMILY_NO);
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
