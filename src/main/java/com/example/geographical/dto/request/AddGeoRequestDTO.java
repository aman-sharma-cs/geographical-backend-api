package com.example.geographical.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGeoRequestDTO {

    private String username;
    private Double latitude;
    private Double longitude;
    private String description;
    private String remarks;

}
