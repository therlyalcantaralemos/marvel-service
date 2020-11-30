package br.com.marvel.service.comic.factories;

import br.com.marvel.service.comic.models.CharacterItem;
import br.com.marvel.service.comic.services.client.CharacterDTO;

public class CharacterFactory {

    public static CharacterDTO getCharacterDTO() {
        return CharacterDTO.builder()
                .id("123")
                .name("Spider man")
                .description("from queens ny")
                .year(1980)
                .build();
    }

    public static CharacterItem getCharacterItem(CharacterDTO characterDTO){
        return CharacterItem.builder()
                .id(characterDTO.getId())
                .name(characterDTO.getName())
                .build();
    }

}
