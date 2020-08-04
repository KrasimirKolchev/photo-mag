package com.krasimirkolchev.photomag.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid product id!")
public class ProductNotFoundException extends BaseException {
    public ProductNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
