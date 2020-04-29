package br.com.marvelservice.models.events;

import br.com.marvelservice.models.CharacterItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComicCreatedEvent {
    private Integer number;
    private String title;
    private String description;
    private List<CharacterItem> characters;
    private List<String> author;
    private LocalDateTime release;
    private String id;
    private Boolean active;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
