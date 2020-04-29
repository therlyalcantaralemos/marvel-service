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
@AllArgsConstructor
@NoArgsConstructor
public class ComicItem {
    private String id;
    private Integer number;
    private String title;
    private String description;
    private List<String> author;
    private LocalDateTime release;
}
