package br.com.marvel.service.comic.factories;

import br.com.marvel.service.comic.controllers.dto.CreateUpdateComicDTO;
import br.com.marvel.service.comic.models.CharacterItem;
import br.com.marvel.service.comic.models.Comic;

import java.util.List;

public class ComicFactory {

    public static Comic getComic() {
        Comic comic = Comic.builder()
                .number(10)
                .title("the amazing spider-man")
                .description("hero from new york")
                .author(List.of("stan lee"))
                .characters(
                        List.of(CharacterItem.builder()
                                .id("123")
                                .name("Spider-man")
                                .build())
                )
                .build();
        comic.setId("1");
        return comic;
    }

    public static Comic getRequestComic() {
        return Comic.builder()
                .number(10)
                .title("the amazing spider-man")
                .description("hero from new york")
                .author(List.of("stan lee"))
                .build();
    }

    public static Comic getRequestUpdateComic() {
        return Comic.builder()
                .number(10)
                .title("the spider-man")
                .description("hero from queens")
                .author(List.of("steve ditko"))
                .build();
    }

    public static CreateUpdateComicDTO getCreateUpdateComicDTO() {
        return CreateUpdateComicDTO.builder()
                .number(10)
                .title("the amazing spider-man")
                .description("hero from new york")
                .author(List.of("stan lee"))
                .characters(List.of("123"))
                .build();
    }

    public static CharacterItem getCharacterItem(){
        return CharacterItem.builder()
                .id("123")
                .name("Spider-man")
                .build();
    }
}
