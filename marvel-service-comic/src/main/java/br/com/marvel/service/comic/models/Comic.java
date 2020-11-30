package br.com.marvel.service.comic.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Comic extends GenericModel{
    private Integer number;
    private String title;
    private String description;
    private List<String> author;
    private LocalDateTime release;
    private List<CharacterItem> characters;

    public void updateWith(Comic otherComic){
        this.number = otherComic.getNumber();
        this.title = otherComic.getTitle();
        this.description = otherComic.getDescription();
        this.author = otherComic.getAuthor();
        this.release = otherComic.getRelease();
        this.characters = otherComic.getCharacters();
    }

}
