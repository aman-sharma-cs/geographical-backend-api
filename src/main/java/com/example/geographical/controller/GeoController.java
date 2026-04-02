package com.example.geographical.controller;

import com.example.geographical.dto.request.AddGeoRequestDTO;
import com.example.geographical.model.GeoLocation;
import com.example.geographical.model.User;
import com.example.geographical.model.Log;
import com.example.geographical.repository.GeoLocationRepository;
import com.example.geographical.repository.UserRepository;
import com.example.geographical.repository.LogRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class GeoController {

    private final GeoLocationRepository geoRepository;
    private final UserRepository userRepository;
    private final LogRepository logRepository;   // ✅ added

    public GeoController(GeoLocationRepository geoRepository,
                         UserRepository userRepository,
                         LogRepository logRepository) {   // ✅ added
        this.geoRepository = geoRepository;
        this.userRepository = userRepository;
        this.logRepository = logRepository;   // ✅ added
    }

    // ====== GEO LOCATIONS ======

    // Save new location
    @PostMapping("/geo/save")
    public ResponseEntity<?> saveLocation(@RequestBody GeoLocation geo) {
        geo.setStatus(1);
        GeoLocation saved = geoRepository.save(geo);

        // ✅ LOGGING
        Log log = new Log();
        log.setAction("SAVE");
        log.setEntity("GeoLocation");
        log.setStatus("SUCCESS");
        log.setTimestamp(LocalDateTime.now().toString());
        logRepository.save(log);

        return ResponseEntity.ok(saved);
    }

    // Add new location from frontend (with username)
    @PostMapping("/geo/add")
    public ResponseEntity<?> addLocation(@RequestBody AddGeoRequestDTO request) {

        Optional<User> optionalUser = userRepository.findByUserName(request.getUsername());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User not found with username: " + request.getUsername());
        }

        User user = optionalUser.get();

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setUser(user);
        geoLocation.setLatitude(request.getLatitude());
        geoLocation.setLongitude(request.getLongitude());
        geoLocation.setDescription(request.getDescription());
        geoLocation.setRemarks(request.getRemarks());
        geoLocation.setStatus(1);

        GeoLocation saved = geoRepository.save(geoLocation);

        // ✅ LOGGING
        Log log = new Log();
        log.setAction("ADD");
        log.setEntity("GeoLocation");
        log.setStatus("SUCCESS");
        log.setTimestamp(LocalDateTime.now().toString());
        System.out.println("LOGGING TRIGGERED");
        logRepository.save(log);

        return ResponseEntity.ok(saved);
    }

    // Get all locations (admin dashboard)
    @GetMapping("/geo/all")
    public List<GeoLocation> getAllLocations() {
        return geoRepository.findAll();
    }

    // Get locations by username (user dashboard)
    @GetMapping("/geo/user/{username}")
    public List<GeoLocation> getLocationsByUser(@PathVariable String username) {
        return geoRepository.findByUserUserName(username);
    }

    // ====== USERS ======

    // Get all users (admin dashboard)
    @GetMapping("/user/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @PutMapping("/geo/update/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable Long id, @RequestBody GeoLocation updatedGeo) {

        Optional<GeoLocation> optionalGeo = geoRepository.findById(id);

        if (optionalGeo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("GeoLocation not found with id: " + id);
        }

        GeoLocation existing = optionalGeo.get();
        existing.setLatitude(updatedGeo.getLatitude());
        existing.setLongitude(updatedGeo.getLongitude());
        existing.setDescription(updatedGeo.getDescription());
        existing.setRemarks(updatedGeo.getRemarks());

        GeoLocation saved = geoRepository.save(existing);

        // ✅ LOGGING
        Log log = new Log();
        log.setAction("UPDATE");
        log.setEntity("GeoLocation");
        log.setStatus("SUCCESS");
        log.setTimestamp(LocalDateTime.now().toString());
        logRepository.save(log);

        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/geo/delete/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {

        Optional<GeoLocation> optionalGeo = geoRepository.findById(id);

        if (optionalGeo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("GeoLocation not found with id: " + id);
        }

        geoRepository.deleteById(id);

        // ✅ LOGGING
        Log log = new Log();
        log.setAction("DELETE");
        log.setEntity("GeoLocation");
        log.setStatus("SUCCESS");
        log.setTimestamp(LocalDateTime.now().toString());
        logRepository.save(log);

        return ResponseEntity.ok("GeoLocation deleted successfully");
    }
}