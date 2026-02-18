package com.example.geographical.controller.view;

import com.example.geographical.dto.request.GeographicalDataRequestDTO;
import com.example.geographical.service.GeographicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/geo")
public class GeographicalViewController {

    @Autowired
    private GeographicalService geographicalService;

    @PostMapping("/save")
    public String saveFormData(GeographicalDataRequestDTO dto) {

        geographicalService.addGeographicalData(dto);

        return "redirect:/success.html";
    }
}
