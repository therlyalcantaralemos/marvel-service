package br.com.marvel.service.character.factories;

import br.com.marvel.service.character.controllers.dto.CreateUpdateCharacterDTO;
import br.com.marvel.service.character.models.Character;

public class CharacterFactory {

    public static Character getCharacter(){
        Character character = Character.builder()
                .name("Spider-man")
                .description("hero from new york")
                .year(1980)
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
