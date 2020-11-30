package br.com.marvel.service.comic.services;

import br.com.marvel.service.comic.exceptions.ServiceNotAvailableException;
import br.com.marvel.service.comic.factories.CharacterFactory;
import br.com.marvel.service.comic.models.CharacterItem;
import br.com.marvel.service.comic.services.client.CharacterDTO;
import br.com.marvel.service.comic.services.client.CharacterServiceClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CharacterServiceTest {

    @InjectMocks
    private CharacterService characterService;

    @Mock
    private CharacterServiceClient characterServiceClient;

    @Mock
    private ModelMapper modelMapper;

    @Test
    @DisplayName("When I try get a character by id Then a character item is returned")
    public void shouldGetCharacterById(){
        CharacterDTO characterDTO = CharacterFactory.getCharacterDTO();
        CharacterItem characterItem = CharacterFactory.getCharacterItem(characterDTO);

        Mockito.when(characterServiceClient.getById(characterDTO.getId())).thenReturn(characterDTO);
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(characterItem);

        CharacterItem getCharacterItem = characterService.getById(characterDTO.getId());

        Assertions.assertThat(getCharacterItem.getId()).isEqualTo(characterDTO.getId());
    }

    @Test
    @DisplayName("When I try get a character by id And the character service is not available Then it should throw a not available error")
    public void shouldTryGetCharacterByIdAndServiceNotAvailableException(){

        Mockito.when(characterServiceClient.getById(Mockito.any())).thenThrow(ServiceNotAvailableException.class);

        Assertions.assertThatThrownBy(() ->  characterService.getById("123"))
                .isInstanceOf(ServiceNotAvailableException.class);

    }

}
