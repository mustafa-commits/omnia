package com.sc.demo.controller;

import com.sc.demo.SecuredRestController;
import com.sc.demo.config.OpenApi30Config;
import com.sc.demo.model.dto.FamilyInfo.ChildrenAndMailyFamilyMambersResponse;
import com.sc.demo.model.dto.FamilyInfo.FamilyInfoBasicResponse;
import com.sc.demo.model.dto.FamilyInfo.FamilyInfoHousingResponse;
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
    @GetMapping("/V1/sc/api/getFamilyBasicInformation")
    public List<FamilyInfoBasicResponse> getFamilyBasicInformation(@RequestHeader(name = "authorization") String token){
        return familyInfoService.getFamilyBasicInfo(token);
    }

    // جلب بيانات سكن العائلة
    @GetMapping("/V1/sc/api/getFamilyHousingInformation")
    public List<FamilyInfoHousingResponse> getFamilyHousingInformation(@RequestHeader(name = "authorization") String token){
        return familyInfoService.getFamilyHousingInfo(token);
    }

    // عدد افراد العائلة + عدد الايتام
    @GetMapping("/V1/sc/api/getChildrenAndMailyFamilyMambersResponse")
    public List<ChildrenAndMailyFamilyMambersResponse> getChildrenAndMailyFamilyMambersResponse(@RequestHeader(name = "authorization") String token){
        return familyInfoService.getChildrenAndMailyFamilyMambersResponse(token);
    }
}
