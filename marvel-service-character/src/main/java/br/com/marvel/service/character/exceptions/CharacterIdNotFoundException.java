package br.com.marvel.service.character.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CharacterIdNotFoundException extends RuntimeException{
    public CharacterIdNotFoundException(String msg) {
        super(msg);
    }

    public CharacterIdNotFoundException() {
    }
}