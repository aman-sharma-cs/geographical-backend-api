package com.example.geographical.repository;

import com.example.geographical.model.GeoLocation;
import com.example.geographical.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeoLocationRepository extends JpaRepository<GeoLocation, Long> {

    List<GeoLocation> findByUserUserName(String userName);
    void deleteByUserId(Long userId);

}