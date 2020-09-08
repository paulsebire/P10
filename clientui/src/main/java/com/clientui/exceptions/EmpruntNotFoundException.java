package com.clientui.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmpruntNotFoundException extends RuntimeException {

public EmpruntNotFoundException(String message) {
    super(message);
}
}
