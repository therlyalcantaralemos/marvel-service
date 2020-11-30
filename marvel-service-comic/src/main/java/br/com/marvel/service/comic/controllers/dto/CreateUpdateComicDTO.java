package br.com.marvel.service.comic.controllers.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class CreateUpdateComicDTO {
    @NotNull
    private Integer number;
    @NotEmpty
    private String title;
    @NotBlank
    private String description;
    @NotEmpty
    private List<String> author;
    @NotEmpty
    private List<String> characters;
}
