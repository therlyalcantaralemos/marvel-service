package br.com.marvel.service.comic.services;


import br.com.marvel.service.comic.exceptions.ComicAlreadyExistsException;
import br.com.marvel.service.comic.exceptions.ComicIdNotFoundException;
import br.com.marvel.service.comic.models.Comic;
import br.com.marvel.service.comic.repositories.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComicService {
    private final ComicRepository comicRepository;
    private final CharacterService characterService;

    @Autowired
    public ComicService(ComicRepository comicRepository, CharacterService characterService) {
        this.comicRepository = comicRepository;
        this.characterService = characterService;
    }

    public Comic create(Comic comic, List<String> characters) {
        comicRepository.findByNumberAndTitleAndActiveTrue(comic.getNumber(), comic.getTitle())
                .ifPresent(findComic -> { throw new ComicAlreadyExistsException(); });
        comic.setCharacters(characterService.getByListIds(characters));
        return comicRepository.save(comic);
    }

    public Comic update(String id, Comic comic, List<String> characters) {
        Comic existComic = getById(id);
        comic.setCharacters(characterService.getByListIds(characters));
        existComic.updateWith(comic);
        return comicRepository.save(existComic);
    }

    public Comic delete(String id) {
        Comic existComic = getById(id);
        existComic.deactivate();
        return comicRepository.save(existComic);
    }

    public Page<Comic> getAll(Pageable pageable) {
        return comicRepository.findAll(pageable);
    }

    public Comic getById(String id) {
        return comicRepository.findByIdAndActiveTrue(id).orElseThrow(ComicIdNotFoundException::new);
    }

}
