package br.com.marvel.service.comic.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ComicAlreadyExistsException extends RuntimeException{
    public ComicAlreadyExistsException(String msg) {
        super(msg);
    }

    public ComicAlreadyExistsException() {
    }
}
