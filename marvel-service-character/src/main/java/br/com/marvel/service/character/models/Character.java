package br.com.marvel.service.character.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Character extends GenericModel{
    private String name;
    private String description;
    private Integer year;

    public void updateWith(Character otherCharacter){
        this.name = otherCharacter.getName();
        this.description = otherCharacter.getDescription();
        this.year = otherCharacter.getYear();

    }
}
