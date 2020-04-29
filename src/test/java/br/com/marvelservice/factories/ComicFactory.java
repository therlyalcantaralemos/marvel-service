package br.com.marvelservice.factories;

import br.com.marvelservice.controllers.dto.CreateUpdateComicDTO;
import br.com.marvelservice.models.CharacterItem;
import br.com.marvelservice.models.Comic;
import br.com.marvelservice.models.events.ComicCreatedEvent;
import br.com.marvelservice.models.events.ComicDeletedEvent;

import java.util.List;

public class ComicFactory {

    public static Comic getComic(){
        Comic comic = Comic.builder()
                .number(10)
                .title("the amazing spider-man")
                .description("hero from new york")
                .characters(List.of(CharacterItem
                        .builder()
                        .id("1")
                        .name("Spider-man")
                        .description("the new york")
                        .year(1980)
                        .build()
                        )
                )
                .author(List.of("stan lee"))
                .build();
        comic.setId("1");
        return comic;
    }

    public static Comic getRequestComic(){
        return Comic.builder()
                .number(10)
                .title("the amazing spider-man")
                .description("hero from new york")
                .author(List.of("stan lee"))
                .build();
    }

    public static Comic getRequestUpdateComic(){
        return Comic.builder()
                .number(10)
                .title("the spider-man")
                .description("hero from queens")
                .author(List.of("steve ditko"))
                .build();
    }

    public static CreateUpdateComicDTO getCreateUpdateComicDTO(){
        return CreateUpdateComicDTO.builder()
                .number(10)
                .title("the amazing spider-man")
                .description("hero from new york")
                .characters(List.of("1"))
                .author(List.of("stan lee"))
                .build();
    }

    public static ComicDeletedEvent getComicDeletedEvent(){
        return ComicDeletedEvent.builder()
                .id("1")
                .number(10)
                .title("the amazing spider-man")
                .description("hero from new york")
                .characters(List.of(CharacterItem
                                .builder()
                                .id("1")
                                .name("Spider-man")
                                .description("the new york")
                                .year(1980)
                                .build()
                        )
                )
                .author(List.of("stan lee"))
                .build();
    }

    public static ComicCreatedEvent getComicCreatedEvent(){
        return ComicCreatedEvent.builder()
                .id("1")
                .number(10)
                .title("the amazing spider-man")
                .description("hero from new york")
                .characters(List.of(CharacterItem
                                .builder()
                                .id("1")
                                .name("Spider-man")
                                .description("the new york")
                                .year(1980)
                                .build()
                        )
                )
                .author(List.of("stan lee"))
                .build();
    }
}
