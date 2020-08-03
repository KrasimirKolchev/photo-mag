package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "brands")
public class Brand extends BaseEntity {
    private String name;

    public Brand() {
    }

    @Column(name = "brand", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
