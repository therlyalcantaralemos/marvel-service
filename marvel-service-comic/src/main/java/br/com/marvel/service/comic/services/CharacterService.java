package br.com.marvel.service.comic.services;

import br.com.marvel.service.comic.exceptions.ServiceNotAvailableException;
import br.com.marvel.service.comic.models.CharacterItem;
import br.com.marvel.service.comic.services.client.CharacterServiceClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterService {

    private final CharacterServiceClient characterServiceClient;
    private final ModelMapper modelMapper;

    @Autowired
    public CharacterService(
            CharacterServiceClient characterServiceClient,
            ModelMapper modelMapper
    ) {
        this.characterServiceClient = characterServiceClient;
        this.modelMapper = modelMapper;
    }

    public CharacterItem getById(String id){
        try {
            return modelMapper.map(characterServiceClient.getById(id), CharacterItem.class);
        }catch (RuntimeException e){
            throw new ServiceNotAvailableException();
        }
    }

    public List<CharacterItem> getByListIds(List<String> characterIds){
        return characterIds.stream().map(this::getById).collect(Collectors.toList());
    }

}
