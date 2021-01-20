package com.krasimirkolchev.photomag.models.bindingModels;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExpOrdersDatesBindingModel {
    private String expFrom;
    private String expTo;

    public ExpOrdersDatesBindingModel() {
        this.expFrom = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.expTo = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String getExpFrom() {
        return expFrom;
    }

    public void setExpFrom(String expFrom) {
        this.expFrom = expFrom;
    }

    public String getExpTo() {
        return expTo;
    }

    public void setExpTo(String expTo) {
        this.expTo = expTo;
    }
}
