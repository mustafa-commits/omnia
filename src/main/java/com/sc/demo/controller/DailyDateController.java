package com.sc.demo.controller;

import com.sc.demo.Scheduled.DailyDateScheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class DailyDateController {

    @Autowired
    private DailyDateScheduled dailyDateScheduled;

    // اظهار تاريخ اليوم بالهجري
    @GetMapping("/V1/api/sc/arabicDate")
    public List<String> getHtml2() throws InterruptedException {
        if (dailyDateScheduled.data == null) {
            return dailyDateScheduled.fetchDataFromSource();
        } else
            return dailyDateScheduled.data;
    }
}
