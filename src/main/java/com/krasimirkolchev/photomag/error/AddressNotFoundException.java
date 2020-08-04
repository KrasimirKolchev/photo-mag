package com.krasimirkolchev.photomag.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid address id")
public class AddressNotFoundException extends BaseException {
    public AddressNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
