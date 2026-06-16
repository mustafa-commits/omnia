package com.sc.demo.controller;

import com.sc.demo.model.dto.statistics.AppUserStatisticsResponse;
import com.sc.demo.model.dto.statistics.StatisticsUsesResponse;
import com.sc.demo.service.Statistics.AppUserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class AppUserStatisticsController {

    @Autowired
    private AppUserStatisticsService appUserStatisticsService;

    // احصائية مستخدمي التطبيق
    @GetMapping("/V1/api/sc/getStatisticsPhoneUsers")
    public List<AppUserStatisticsResponse> getStatisticsPhoneUsers(){
        return appUserStatisticsService.getStatisticsPhoneUsers();
    }

    // احصائية نسبة الاستخدام
    @GetMapping("/V1/api/sc/getStatisticsUses")
    public List<StatisticsUsesResponse> getStatisticsUses(){
        return appUserStatisticsService.getStatisticsUses();
    }
}
