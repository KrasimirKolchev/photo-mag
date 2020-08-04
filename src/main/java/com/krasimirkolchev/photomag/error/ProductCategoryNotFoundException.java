package com.krasimirkolchev.photomag.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid category id")
public class ProductCategoryNotFoundException extends BaseException {
    public ProductCategoryNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
