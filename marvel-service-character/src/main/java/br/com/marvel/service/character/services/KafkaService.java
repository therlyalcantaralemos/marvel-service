package br.com.marvel.service.character.services;

import br.com.marvel.service.character.exceptions.CharacterEventWriteValueAsStringException;
import br.com.marvel.service.character.models.Character;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendEvent(String topic, Character character) {
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(character));
        }catch (JsonProcessingException ex){
            throw new CharacterEventWriteValueAsStringException();
        }
    }
}
