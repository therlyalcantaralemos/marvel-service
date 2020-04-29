package br.com.marvelservice.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CharacterAlreadyExistsException extends RuntimeException{
    public CharacterAlreadyExistsException(String msg) {
        super(msg);
    }

    public CharacterAlreadyExistsException() {
    }
}
