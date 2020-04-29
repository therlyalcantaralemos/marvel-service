package br.com.marvelservice.services;

import br.com.marvelservice.exceptions.CharacterAlreadyExistsException;
import br.com.marvelservice.exceptions.CharacterIdNotFoundException;
import br.com.marvelservice.models.Character;
import br.com.marvelservice.models.CharacterItem;
import br.com.marvelservice.models.ComicItem;
import br.com.marvelservice.models.events.ComicCreatedEvent;
import br.com.marvelservice.models.events.ComicDeletedEvent;
import br.com.marvelservice.models.events.ComicUpdatedEvent;
import br.com.marvelservice.repositories.CharacterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CharacterService(CharacterRepository characterRepository, ModelMapper modelMapper) {
        this.characterRepository = characterRepository;
        this.modelMapper = modelMapper;
    }

    public Character create(Character character) {
        characterRepository.findByNameAndActiveTrue(character.getName())
                .ifPresent(findCharacter -> { throw new CharacterAlreadyExistsException(); });
        return characterRepository.save(character);
    }

    public Character update(String id, Character character) {
        Character existCharacter = getById(id);
        existCharacter.updateWith(character);
        return characterRepository.save(character);
    }

    public Character delete(String id){
        Character existCharacter = getById(id);
        existCharacter.deactivate();
        return characterRepository.save(existCharacter);
    }

    public Character getById(String id){
        return characterRepository.findByIdAndActiveTrue(id).orElseThrow(CharacterIdNotFoundException:: new);
    }

    public Page<Character> getAll(Pageable pageable){
        return characterRepository.findAll(pageable);
    }

    @EventListener
    @Async
    public void onComicCreatedListener(ComicCreatedEvent comicCreatedEvent){
        addComicCreatedListener(comicCreatedEvent);
    }

    public List<Character> addComicCreatedListener(ComicCreatedEvent comicCreatedEvent) {
        List<String> charactersId = comicCreatedEvent.getCharacters().stream().map(CharacterItem::getId).collect(Collectors.toList());
        List<Character> characters = characterRepository.findByIdInAndActiveTrue(charactersId)
                .stream().peek(character -> {
                    character.getComics().add(modelMapper.map(comicCreatedEvent, ComicItem.class));
                }).collect(Collectors.toList());
        return characterRepository.saveAll(characters);
    }

    @EventListener
    @Async
    public void onComicUpdatedListener(ComicUpdatedEvent comicUpdatedEvent){
        addComicUpdatedListener(comicUpdatedEvent);
    }

    public void addComicUpdatedListener(ComicUpdatedEvent comicUpdatedEvent) {
        characterRepository.updateComic(modelMapper.map(comicUpdatedEvent, ComicItem.class));
    }

    @EventListener
    @Async
    public void onComicDeletedListener(ComicDeletedEvent comicDeletedEvent){
        addComicDeletedListener(comicDeletedEvent);
    }

    public List<Character> addComicDeletedListener(ComicDeletedEvent comicDeletedEvent) {
        List<String> charactersId = comicDeletedEvent.getCharacters().stream().map(CharacterItem::getId).collect(Collectors.toList());
        List<Character> characters = characterRepository.findByIdInAndActiveTrue(charactersId).stream()
                .peek(character -> character.removeComic(comicDeletedEvent.getId()))
                .collect(Collectors.toList());
        return characterRepository.saveAll(characters);
    }

}
