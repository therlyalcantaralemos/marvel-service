package br.com.marvel.service.character.event;

import br.com.marvel.service.character.models.Character;
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
