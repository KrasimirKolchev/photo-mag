package com.krasimirkolchev.photomag.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid brand id")
public class BrandNotFoundException extends BaseException {
    public BrandNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
