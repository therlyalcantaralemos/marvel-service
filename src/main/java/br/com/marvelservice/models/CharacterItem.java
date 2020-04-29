package br.com.marvelservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterItem {
    private String id;
    private String name;
    private String description;
    private Integer year;
}
