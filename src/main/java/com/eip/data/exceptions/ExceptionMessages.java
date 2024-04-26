package com.eip.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ExceptionMessages {
    public static final String SOME_PARAMETERS_INVALID = "SOME_PARAMETERS_INVALID";
}
