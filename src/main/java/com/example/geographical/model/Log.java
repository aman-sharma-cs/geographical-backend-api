package com.example.geographical.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "logs")
public class Log {
    @Id
    private String id;
    private String action;
    private String entity;
    private String status;
    private String timestamp;
}