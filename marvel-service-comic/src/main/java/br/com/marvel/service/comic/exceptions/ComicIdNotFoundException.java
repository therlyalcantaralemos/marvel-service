package br.com.marvel.service.comic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ComicIdNotFoundException extends RuntimeException{
    public ComicIdNotFoundException(String msg) {
        super(msg);
    }

    public ComicIdNotFoundException() {
    }
}