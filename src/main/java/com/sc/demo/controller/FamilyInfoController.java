package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.model.dto.familyInfo.ChildrenAndMailyFamilyMembersResponse;
import com.sc.demo.model.dto.familyInfo.FamilyInfoBasicResponse;
import com.sc.demo.model.dto.familyInfo.FamilyInfoHousingResponse;
import com.sc.demo.model.users.PhoneType;
import com.sc.demo.service.users.FamilyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class FamilyInfoController implements SecuredRestController {

    @Autowired
    private FamilyInfoService familyInfoService;

    // جلب البيانات الاساسية للعائلة مثل ال(اسم, عمر, رقم هاتف, الخ .....)
    @GetMapping("/V1/api/sc/getFamilyBasicInformation")
    public List<FamilyInfoBasicResponse> getFamilyBasicInformation(@RequestHeader(name = "authorization") String token){
        return familyInfoService.getFamilyBasicInfo(token);
    }

    // جلب بيانات سكن العائلة
    @GetMapping("/V1/api/sc/getFamilyHousingInformation")
    public List<FamilyInfoHousingResponse> getFamilyHousingInformation(@RequestHeader(name = "authorization") String token){
        return familyInfoService.getFamilyHousingInfo(token);
    }

    // عدد افراد العائلة + عدد الايتام
    @GetMapping("/V1/api/sc/getChildrenAndMailyFamilyMembersResponse")
    public List<ChildrenAndMailyFamilyMembersResponse> getChildrenAndMailyFamilyMembersResponse(@RequestHeader(name = "authorization") String token){
        return familyInfoService.getChildrenAndMailyFamilyMembersResponse(token);
    }
}
