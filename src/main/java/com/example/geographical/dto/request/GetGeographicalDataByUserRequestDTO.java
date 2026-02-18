package com.example.geographical.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetGeographicalDataByUserRequestDTO {
    private Long gdId;
    private String uuid;
    private String userName;
    private Double latitude;
    private Double longitude;
    private String description;
    private String remarks;
}
