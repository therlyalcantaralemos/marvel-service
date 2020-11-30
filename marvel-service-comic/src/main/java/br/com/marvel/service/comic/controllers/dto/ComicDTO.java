package br.com.marvel.service.comic.controllers.dto;

import br.com.marvel.service.comic.models.CharacterItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComicDTO {
    private String id;
    private Integer number;
    private String title;
    private String description;
    private List<String> author;
    private List<CharacterItem> characters;

}
