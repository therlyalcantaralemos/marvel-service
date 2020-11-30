package br.com.marvel.service.character.services;

import br.com.marvel.service.character.exceptions.CharacterAlreadyExistsException;
import br.com.marvel.service.character.exceptions.CharacterIdNotFoundException;
import br.com.marvel.service.character.models.Character;
import br.com.marvel.service.character.repositories.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final KafkaService kafkaService;

    @Value("kafka.topic.character-update-events")
    public String updatedTopic;

    @Value("kafka.topic.character-remove-events")
    public String removedTopic;

    @Autowired
    public CharacterService(CharacterRepository characterRepository, KafkaService kafkaService) {
        this.characterRepository = characterRepository;
        this.kafkaService = kafkaService;
    }

    public Character create(Character character) {
        verifyCharacterNameAlreadyExists(character.getName());
        return characterRepository.save(character);
    }

    public Character update(String id, Character character) {
        Character existCharacter = getById(id);
        existCharacter.updateWith(character);
        Character updateCharacter = characterRepository.save(existCharacter);
        kafkaService.sendEvent(updatedTopic, updateCharacter);
        return updateCharacter;
    }

    public Character delete(String id) {
        Character existCharacter = getById(id);
        existCharacter.deactivate();
        Character deactivateCharacter = characterRepository.save(existCharacter);
        kafkaService.sendEvent(removedTopic, deactivateCharacter);
        return deactivateCharacter;
    }

    public Character getById(String id) {
        return characterRepository.findByIdAndActiveTrue(id).orElseThrow(CharacterIdNotFoundException::new);
    }

    private void verifyCharacterNameAlreadyExists(String name) {
        characterRepository.findByNameAndActiveTrue(name).ifPresent(character -> {
            throw new CharacterAlreadyExistsException();
        });
    }

    public Page<Character> getAll(Pageable pageable) {
        return characterRepository.findAll(pageable);
    }

}
