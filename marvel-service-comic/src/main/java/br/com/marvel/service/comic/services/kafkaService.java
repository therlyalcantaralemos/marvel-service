package br.com.marvel.service.comic.services;

import br.com.marvel.service.comic.event.CharacterEventWriteValueAsStringException;
import br.com.marvel.service.comic.models.Character;
import br.com.marvel.service.comic.repositories.ComicRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class kafkaService {
    private final ComicRepository comicRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public kafkaService(ComicRepository comicRepository, ObjectMapper objectMapper) {
        this.comicRepository = comicRepository;
        this.objectMapper = objectMapper;
    }

    public Character getCharacter(String event){
        try{
            return objectMapper.readValue(event, Character.class);
        }catch (JsonProcessingException ex){
            throw new CharacterEventWriteValueAsStringException();
        }
    }

    @KafkaListener(topics = "kafka.topic.character-update-events")
    public void onCharacterUpdateEvent(@Payload String event) {
        Character character = getCharacter(event);
        comicRepository.updateCharacterItemInAllComics(character.getId(), character.getName());
    }

    @KafkaListener(topics = "kafka.topic.character-remove-events")
    public void onCharacterRemoveEvent(@Payload String event) {
        Character character = getCharacter(event);
        comicRepository.removeCharacterItemInAllComics(character.getId());
    }
}
