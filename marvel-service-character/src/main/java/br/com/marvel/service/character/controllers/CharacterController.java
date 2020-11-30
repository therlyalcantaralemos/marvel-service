package br.com.marvel.service.character.controllers;

import br.com.marvel.service.character.controllers.dto.CharacterDTO;
import br.com.marvel.service.character.controllers.dto.CreateUpdateCharacterDTO;
import br.com.marvel.service.character.models.Character;
import br.com.marvel.service.character.services.CharacterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/public/characters")
public class CharacterController {

    private final CharacterService characterService;
    private final ModelMapper modelMapper;

    @Autowired
    public CharacterController(CharacterService characterService, ModelMapper modelMapper) {
        this.characterService = characterService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CharacterDTO create(@Valid @RequestBody CreateUpdateCharacterDTO createUpdateCharacterDTO){
        Character createdCharacter = characterService.create(modelMapper.map(createUpdateCharacterDTO, Character.class));
        return modelMapper.map(createdCharacter, CharacterDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CharacterDTO update(@PathVariable String id , @Valid @RequestBody CreateUpdateCharacterDTO createUpdateCharacterDTO){
        Character createdCharacter = characterService.update(id, modelMapper.map(createUpdateCharacterDTO, Character.class));
        return modelMapper.map(createdCharacter, CharacterDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id){
        characterService.delete(id);
    }

    @GetMapping
    public Page<CharacterDTO> getAll(Pageable pageable){
        return characterService.getAll(pageable).map(entity ->  modelMapper.map(entity, CharacterDTO.class));
    }

    @GetMapping("/{id}")
    public CharacterDTO getById(@PathVariable String id){
        return modelMapper.map(characterService.getById(id), CharacterDTO.class);
    }
}
