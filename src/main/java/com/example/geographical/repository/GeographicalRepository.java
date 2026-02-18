package com.example.geographical.repository;

import com.example.geographical.dto.projection.GeographicalDataProjectionDTO;
import com.example.geographical.model.GeographicalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GeographicalRepository extends JpaRepository<GeographicalData, Long> {

    // For fetching with projection
    @Query("SELECT NEW com.example.geographical.dto.projection.GeographicalDataProjectionDTO(" +
            "gd.userName, gd.latitude, gd.longitude, gd.description, gd.remarks) " +
            "FROM GeographicalData gd WHERE gd.uuid = :uuid")
    List<GeographicalDataProjectionDTO> findByUuid(String uuid);

    @Query("SELECT NEW com.example.geographical.dto.projection.GeographicalDataProjectionDTO(" +
            "gd.userName, gd.latitude, gd.longitude, gd.description, gd.remarks) " +
            "FROM GeographicalData gd ORDER BY gd.gdId DESC")
    List<GeographicalDataProjectionDTO> findAllOrdered();


    GeographicalData findFirstByGdId(long gdId);
}


