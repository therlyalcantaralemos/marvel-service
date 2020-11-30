package br.com.marvel.service.comic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceNotAvailableException extends RuntimeException {
    public ServiceNotAvailableException(String msg) {
        super(msg);
    }

    public ServiceNotAvailableException() {
    }
}
