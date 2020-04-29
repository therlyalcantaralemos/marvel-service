package br.com.marvelservice.services;

import br.com.marvelservice.exceptions.ComicAlreadyExistsException;
import br.com.marvelservice.exceptions.ComicIdNotFoundException;
import br.com.marvelservice.models.CharacterItem;
import br.com.marvelservice.models.Comic;
import br.com.marvelservice.models.events.ComicCreatedEvent;
import br.com.marvelservice.models.events.ComicDeletedEvent;
import br.com.marvelservice.models.events.ComicUpdatedEvent;
import br.com.marvelservice.repositories.ComicRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComicService {
    private final ComicRepository comicRepository;
    private final CharacterService characterService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ComicService(ComicRepository comicRepository, CharacterService characterService, ModelMapper modelMapper, ApplicationEventPublisher applicationEventPublisher) {
        this.comicRepository = comicRepository;
        this.characterService = characterService;
        this.modelMapper = modelMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public Comic create(Comic comic, List<String> charactersId){
        comicRepository.findByNumberAndTitleAndActiveTrue(comic.getNumber(), comic.getTitle())
                .ifPresent(findComic -> {throw new ComicAlreadyExistsException();});

        comic.setCharacters(getCharactersItemsByCharactersId(charactersId));
        Comic createdComic = comicRepository.save(comic);

        applicationEventPublisher.publishEvent(modelMapper.map(createdComic, ComicCreatedEvent.class));
        return createdComic;
    }

    public Comic update(String id, Comic comic, List<String> charactersId){
        Comic existComic = getById(id);
        comic.setCharacters(getCharactersItemsByCharactersId(charactersId));
        existComic.updateWith(comic);

        Comic updatedComic = comicRepository.save(existComic);

        applicationEventPublisher.publishEvent(modelMapper.map(updatedComic, ComicUpdatedEvent.class));
        return updatedComic;
    }

    public Comic delete(String id){
        Comic existComic = getById(id);
        existComic.deactivate();
        Comic deletedComic = comicRepository.save(existComic);

        applicationEventPublisher.publishEvent(modelMapper.map(deletedComic, ComicDeletedEvent.class));
        return deletedComic;
    }

    public Page<Comic> getAll(Pageable pageable){
         return comicRepository.findAll(pageable);
    }

    public Comic getById(String id){
        return comicRepository.findByIdAndActiveTrue(id).orElseThrow(ComicIdNotFoundException::new);
    }

    private List<CharacterItem> getCharactersItemsByCharactersId(List<String> charactersId){
        return charactersId.stream()
                .map(id -> {
                    CharacterItem characterItem = modelMapper.map(characterService.getById(id), CharacterItem.class);
                    characterItem.setId(id);
                    return characterItem;
                })
                .collect(Collectors.toList());
    }

}
