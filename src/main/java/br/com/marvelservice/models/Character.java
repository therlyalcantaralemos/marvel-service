package br.com.marvelservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Character extends GenericModel{
    private String name;
    private String description;
    private Integer year;
    private List<ComicItem> comics = new ArrayList<>();

    public void updateWith(Character otherCharacter){
        this.name = otherCharacter.getName();
        this.description = otherCharacter.getDescription();
        this.year = otherCharacter.getYear();
        this.comics = otherCharacter.getComics();

    }

    public void removeComic(String id) {
        this.comics.removeIf(comic -> comic.getId().equals(id));
    }

}
