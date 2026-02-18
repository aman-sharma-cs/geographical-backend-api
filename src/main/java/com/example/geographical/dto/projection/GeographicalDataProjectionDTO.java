package com.example.geographical.dto.projection;

public class GeographicalDataProjectionDTO {

    private String userName;
    private Double latitude;
    private Double longitude;
    private String description;
    private String remarks;

    public GeographicalDataProjectionDTO(
            String userName,
            Double latitude,
            Double longitude,
            String description,
            String remarks) {

        this.userName = userName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.remarks = remarks;
    }

    public String getUserName() { return userName; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getDescription() { return description; }
    public String getRemarks() { return remarks; }
}
