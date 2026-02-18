package com.example.geographical.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeographicalDataResponseDTO {

    private String userName;
    private Double latitude;
    private Double longitude;
    private String description;
    private String remarks;
}
