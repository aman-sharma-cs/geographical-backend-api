package com.example.geographical.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;


@Builder
@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "geographical_data")
public class GeographicalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gd_id")
    private Long gdId;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "description")
    private String description;

    @Column(name = "remarks")
    private String remarks;

    @Column(name ="created_date")
    private Date createdDate;

    @Column(name ="updated_date")
    private Date updatedDate;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public GeographicalData setDeleted(boolean deleted) {
        isDeleted = deleted;
        return this;
    }




}
