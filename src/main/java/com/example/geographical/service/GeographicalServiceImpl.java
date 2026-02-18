package com.example.geographical.service;

import com.example.geographical.dto.projection.GeographicalDataProjectionDTO;
import com.example.geographical.dto.request.GeographicalDataRequestDTO;
import com.example.geographical.dto.request.GetGeographicalDataByUserRequestDTO;
import com.example.geographical.dto.response.GeographicalDataResponseDTO;
import com.example.geographical.model.GeographicalData;
import com.example.geographical.repository.GeographicalRepository;
import com.example.geographical.response.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GeographicalServiceImpl implements GeographicalService {

    @Autowired
    private GeographicalRepository geographicalRepository;

    @Override
    public ResponseModel addGeographicalData(
            GeographicalDataRequestDTO geographicalDataRequestDTO) {

        try {
            GeographicalData geographicalData = new GeographicalData()
                    .setUuid(geographicalDataRequestDTO.getUuid())
                    .setUserName(geographicalDataRequestDTO.getUserName())
                    .setLatitude(geographicalDataRequestDTO.getLatitude())
                    .setLongitude(geographicalDataRequestDTO.getLongitude())
                    .setDescription(geographicalDataRequestDTO.getDescription())
                    .setRemarks(geographicalDataRequestDTO.getRemarks())
                    .setCreatedDate(new Date())
                    .setUpdatedDate(new Date());
            GeographicalData savedData = geographicalRepository.save(geographicalData);

            return new ResponseModel()
                    .setStatus(200)
                    .setMessage("Data added successfully")
                    .setData(savedData);
        } catch (Exception e) {
            return new ResponseModel()
                    .setStatus(500)
                    .setMessage("Failed to add geographical data")
                    .setData(null);
        }
        }


    @Override
    public ResponseModel getGeographicalDataByUser(String uuid) {

        List<GeographicalDataProjectionDTO> projectionList =
                geographicalRepository.findByUuid(uuid);

        List<GeographicalDataResponseDTO> responseList =
                getGeographicalDataResponseDTOS(projectionList);

        if (!responseList.isEmpty()) {
            return new ResponseModel()
                    .setStatus(200)
                    .setMessage("Success")
                    .setData(responseList);
        }

        return new ResponseModel()
                .setStatus(204)
                .setMessage("Data Not Found")
                .setData(null);
    }


    private static List<GeographicalDataResponseDTO> getGeographicalDataResponseDTOS(List<GeographicalDataProjectionDTO> projectionList) {
        List<GeographicalDataResponseDTO> responseList = new ArrayList<>();

        for (GeographicalDataProjectionDTO dto : projectionList) {

            GeographicalDataResponseDTO responseDTO =
                    new GeographicalDataResponseDTO(
                            dto.getUserName(),
                            dto.getLatitude(),
                            dto.getLongitude(),
                            dto.getDescription(),
                            dto.getRemarks()
                    );

            responseList.add(responseDTO);
        }
        return responseList;
    }

    @Override
    public List<GeographicalDataResponseDTO> getAllGeographicalData() {

        List<GeographicalDataProjectionDTO> projectionList =
                geographicalRepository.findAllOrdered();

        List<GeographicalDataResponseDTO> responseList = new ArrayList<>();

        for (GeographicalDataProjectionDTO dto : projectionList) {

            GeographicalDataResponseDTO responseDTO =
                    new GeographicalDataResponseDTO(
                            dto.getUserName(),
                            dto.getLatitude(),
                            dto.getLongitude(),
                            dto.getDescription(),
                            dto.getRemarks()
                    );

            responseList.add(responseDTO);
        }

        return responseList;
    }

    @Override
    public void updateGeographicalData(GeographicalDataRequestDTO geographicalDataRequestDTO) {

    }

    @Override
    public ResponseModel updateGeographicalData(
            GetGeographicalDataByUserRequestDTO requestDTO) {

        GeographicalData data =
                geographicalRepository.findFirstByGdId(requestDTO.getGdId());

        if (data == null) {
            return new ResponseModel()
                    .setStatus(404)
                    .setMessage("Data not found")
                    .setData(null);
        }

        data.setUserName(requestDTO.getUserName());
        data.setLatitude(requestDTO.getLatitude());
        data.setLongitude(requestDTO.getLongitude());
        data.setDescription(requestDTO.getDescription());
        data.setRemarks(requestDTO.getRemarks());
        data.setUpdatedDate(new Date());

        GeographicalData savedData =  geographicalRepository.save(data);

        return new ResponseModel()
                .setStatus(200)
                .setMessage("Updated Successfully")
                .setData(savedData);
    }
    @Override
    public ResponseModel deleteGeographicalData(Long gdId) {

        GeographicalData data =
                geographicalRepository.findFirstByGdId(gdId);

        data.setDeleted(true);
        data.setUpdatedDate(new Date());

        GeographicalData savedData = geographicalRepository.save(data);

        return new ResponseModel()
                .setStatus(200)
                .setMessage("Deleted Successfully")
                .setData(savedData);
    }



}
