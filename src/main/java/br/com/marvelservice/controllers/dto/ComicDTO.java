package br.com.marvelservice.controllers.dto;

import br.com.marvelservice.models.CharacterItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private List<CharacterItem> characters;
    private List<String> author;
}
