package com.example.code.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "pratical-java2-" )
public class CarPromotion {

    private String type;

    private String description;

    @Id
    private String id;

    public CarPromotion() {
    }

    public CarPromotion(String type, String description, String id) {
        this.type = type;
        this.description = description;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
