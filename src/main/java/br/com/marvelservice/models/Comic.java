package br.com.marvelservice.models;

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
    private List<CharacterItem> characters;
    private List<String> author;
    private LocalDateTime release;

    public void updateWith(Comic otherComic){
        this.number = otherComic.getNumber();
        this.title = otherComic.getTitle();
        this.description = otherComic.getDescription();
        this.characters = otherComic.getCharacters();
        this.author = otherComic.getAuthor();
        this.release = otherComic.getRelease();
    }

}
