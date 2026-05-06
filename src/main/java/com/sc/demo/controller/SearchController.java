package com.sc.demo.controller;

import com.sc.demo.model.dto.Search.SearchRequest;
import com.sc.demo.service.Search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    // استعلام بأسم الوصي
    @GetMapping("/V1/api/sc/SearchByName")
    public List<SearchRequest> SearchByName(@RequestParam (required = false) String GuardianName,
                                            @RequestParam (required = false) Long GuardianId){
        return searchService.SearchByNameOrId(GuardianName, GuardianId);
    }
}
