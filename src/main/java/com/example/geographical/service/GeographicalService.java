package com.example.geographical.service;

import com.example.geographical.dto.request.GeographicalDataRequestDTO;
import com.example.geographical.dto.request.GetGeographicalDataByUserRequestDTO;
import com.example.geographical.dto.response.GeographicalDataResponseDTO;
import com.example.geographical.response.ResponseModel;

import java.util.List;

public interface GeographicalService {


    ResponseModel addGeographicalData(GeographicalDataRequestDTO geographicalDataRequestDTO);

    ResponseModel getGeographicalDataByUser(String uuid);
    List<GeographicalDataResponseDTO> getAllGeographicalData();

    void updateGeographicalData(GeographicalDataRequestDTO geographicalDataRequestDTO);


    ResponseModel updateGeographicalData(
            GetGeographicalDataByUserRequestDTO requestDTO);
    ResponseModel deleteGeographicalData(Long gdId);

}
