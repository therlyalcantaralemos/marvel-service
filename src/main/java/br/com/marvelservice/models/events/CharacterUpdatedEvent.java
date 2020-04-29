package br.com.marvelservice.models.events;

import br.com.marvelservice.models.ComicItem;
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
public class CharacterUpdatedEvent {
    private String name;
    private String description;
    private Integer year;
    private List<ComicItem> comics;
    private String id;
    private Boolean active;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
