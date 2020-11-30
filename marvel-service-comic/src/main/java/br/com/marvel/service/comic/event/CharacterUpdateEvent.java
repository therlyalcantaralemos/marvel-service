package br.com.marvel.service.comic.event;

import br.com.marvel.service.comic.models.Character;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CharacterUpdateEvent implements Event {
    private Character payload;
}
