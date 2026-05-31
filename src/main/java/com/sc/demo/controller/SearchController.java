package com.sc.demo.controller;

import com.sc.demo.config.OpenApi30Config;
import com.sc.demo.model.dto.Search.SearchResponse;
import com.sc.demo.model.dto.Search.SearchResponseV2;
import com.sc.demo.service.Search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class SearchController implements OpenApi30Config {

    @Autowired
    private SearchService searchService;

    // (اسم, رمز الوصي) استعلام بأسم الوصي
    @GetMapping("/V1/api/sc/SearchByName")
    public List<SearchResponse> SearchByName(@RequestParam String GuardianName){
        return searchService.SearchByName(GuardianName);
    }


    // استعلام بأسم الوصي وجلب (اسم, رمز الوصي و اسم دو العلاقة)
    @GetMapping("/V2/api/sc/SearchByNameV2")
    public List<SearchResponseV2> SearchByNameV2(@RequestParam String GuardianName){
        return searchService.SearchByNameV2(GuardianName);
    }
}
