package br.com.marvelservice.factories;

import br.com.marvelservice.controllers.dto.CreateUpdateCharacterDTO;
import br.com.marvelservice.models.Character;

import java.util.ArrayList;

public class CharacterFactory {

    public static Character getCharacter(){
        Character character = Character.builder()
                .name("Spider-man")
                .description("hero from new york")
                .year(1980)
                .comics(new ArrayList<>())
                .build();
        character.setId("1");
        return character;
    }

    public static Character getRequestCharacter(){
        return Character.builder()
                .name("Spider-man")
                .description("hero from new york")
                .year(1980)
                .build();
    }

    public static Character getRequestUpdateCharacter(){
        return Character.builder()
                .name("Wolverine")
                .description("hero from new york")
                .year(1980)
                .build();
    }

    public static CreateUpdateCharacterDTO getCreateUpdateCharacterDTO(){
        return CreateUpdateCharacterDTO.builder()
                .name("Spider-man")
                .description("hero from new york")
                .year(1980)
                .build();
    }
}
