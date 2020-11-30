package br.com.marvel.service.character.controllers.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateUpdateCharacterDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Integer year;
}
