package br.com.marvel.service.character.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CharacterEventWriteValueAsStringException extends RuntimeException {
    public CharacterEventWriteValueAsStringException(String msg) {
        super(msg);
    }

    public CharacterEventWriteValueAsStringException() {
    }
}
