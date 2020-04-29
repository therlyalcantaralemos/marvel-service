package br.com.marvelservice.controllers;

import br.com.marvelservice.controllers.dto.ComicDTO;
import br.com.marvelservice.controllers.dto.CreateUpdateComicDTO;
import br.com.marvelservice.models.Comic;
import br.com.marvelservice.services.ComicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/public/comic")
public class ComicController {

    private final ComicService comicService;
    private final ModelMapper modelMapper;

    @Autowired
    public ComicController(ComicService comicService, ModelMapper modelMapper) {
        this.comicService = comicService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComicDTO create(@Valid @RequestBody CreateUpdateComicDTO createUpdateComicDTO){
        Comic createdComic = comicService.create(modelMapper.map(createUpdateComicDTO, Comic.class), createUpdateComicDTO.getCharacters());
        return modelMapper.map(createdComic, ComicDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ComicDTO update(@PathVariable String id , @Valid @RequestBody CreateUpdateComicDTO createUpdateComicDTO){
        Comic createdCharacter = comicService.update(id, modelMapper.map(createUpdateComicDTO, Comic.class), createUpdateComicDTO.getCharacters());
        return modelMapper.map(createdCharacter, ComicDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){
        comicService.delete(id);
    }

    @GetMapping
    public Page<ComicDTO> getAll(Pageable pageable){
        return comicService.getAll(pageable).map(entity ->  modelMapper.map(entity, ComicDTO.class));
    }

    @GetMapping("/{id}")
    public ComicDTO getById(@PathVariable String id){
        return modelMapper.map(comicService.getById(id), ComicDTO.class);
    }

}
