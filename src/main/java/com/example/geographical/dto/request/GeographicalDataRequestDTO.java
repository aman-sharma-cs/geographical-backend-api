package com.example.geographical.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class GeographicalDataRequestDTO {
    @JsonProperty("uuid")
    String uuid;

    @JsonProperty("user_name")
    String userName;

    @JsonProperty("latitude")
    Double latitude;

    @JsonProperty("longitude")
    Double longitude;

    @JsonProperty("description")
    String description;

    @JsonProperty("remarks")
    String remarks;
}