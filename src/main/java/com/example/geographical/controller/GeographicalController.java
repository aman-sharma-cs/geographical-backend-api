package com.example.geographical.controller;

import com.example.geographical.dto.request.GeographicalDataRequestDTO;
import com.example.geographical.dto.request.GetGeographicalDataByUserRequestDTO;
import com.example.geographical.dto.response.GeographicalDataResponseDTO;
import com.example.geographical.response.ResponseModel;
import com.example.geographical.service.GeographicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geographical")
public class GeographicalController {

    @Autowired
    private GeographicalService geographicalService;

    @PostMapping("/addGeographicalData")
    public ResponseEntity<ResponseModel> addGeographicalData(
            @RequestBody GeographicalDataRequestDTO dto) {

        ResponseModel response = geographicalService.addGeographicalData(dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }




    @GetMapping("/getGeographicalDataByUser/{uuid}")
    public ResponseEntity<?> getGeographicalDataByUser(
            @PathVariable String uuid) {

        return ResponseEntity.ok(
                geographicalService.getGeographicalDataByUser(uuid)
        );
    }


    @GetMapping("/getAllGeographicalData")
    public ResponseEntity<List<GeographicalDataResponseDTO>> getAllGeographicalData() {

        List<GeographicalDataResponseDTO> data =
                geographicalService.getAllGeographicalData();

        return ResponseEntity.ok(data);
    }

    @PutMapping("/updateGeographicalData")
    public ResponseEntity<?> updateGeographicalData(
            @RequestBody GetGeographicalDataByUserRequestDTO dto) {

        return ResponseEntity.ok(
                geographicalService.updateGeographicalData(dto)
        );}
        @DeleteMapping("/deleteGeographicalData/{gdId}")
        public ResponseEntity<?> deleteGeographicalData(
                @PathVariable Long gdId) {

            return ResponseEntity.ok(
                    geographicalService.deleteGeographicalData(gdId)
            );
        }



    }
