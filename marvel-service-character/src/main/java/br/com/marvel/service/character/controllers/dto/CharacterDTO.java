package br.com.marvel.service.character.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterDTO {
    private String id;
    private String name;
    private String description;
    private Integer year;
}
