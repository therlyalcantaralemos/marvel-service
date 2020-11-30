package br.com.marvel.service.character.services;

import br.com.marvel.service.character.exceptions.CharacterAlreadyExistsException;
import br.com.marvel.service.character.exceptions.CharacterIdNotFoundException;
import br.com.marvel.service.character.factories.CharacterFactory;
import br.com.marvel.service.character.models.Character;
import br.com.marvel.service.character.repositories.CharacterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CharacterServiceTest {

    @InjectMocks
    private CharacterService characterService;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private KafkaService kafkaService;

    @Mock
    private ModelMapper modelMapper;

    @Nested
    public class CreateCharacter {
        @Test
        @DisplayName("Given I have a character data When I try to create a character Then a character is created")
        public void shouldCreateCharacter(){
            Character requestCharacter = CharacterFactory.getRequestCharacter();

            when(characterRepository.findByNameAndActiveTrue(any(String.class))).thenReturn(Optional.empty());
            when(characterRepository.save(any(Character.class))).thenAnswer(returnsFirstArg());

            Character createdCharacter = characterService.create(requestCharacter);

            assertThat(createdCharacter.getName()).isEqualTo(requestCharacter.getName());
            assertThat(createdCharacter.getDescription()).isEqualTo(requestCharacter.getDescription());
            assertThat(createdCharacter.getYear()).isEqualTo(requestCharacter.getYear());

            verify(characterRepository, times(1)).save(Mockito.any(Character.class));
        }

        @Test
        @DisplayName("Given I have a character data When I try to create a exists character Then  it should throw a character already exists exception")
        public void shouldNotCreatedCharacterAndExpectedCharacterAlreadyExistsException(){
            Character requestCharacter = CharacterFactory.getRequestCharacter();
            Character character = CharacterFactory.getCharacter();

            when(characterRepository.findByNameAndActiveTrue(any(String.class))).thenReturn(Optional.of(character));

            assertThatThrownBy( () -> characterService.create(requestCharacter))
                    .isInstanceOf(CharacterAlreadyExistsException.class);

            verify(characterRepository, never()).save(Mockito.any(Character.class));
        }
    }

    @Nested
    public class UpdateCharacter {
        @Test
        @DisplayName("Given I have a exists character  When I try to update a character Then a character is updated")
        public void shouldUpdateCharacter(){
            Character character = CharacterFactory.getCharacter();
            Character requestUpdateCharacter = CharacterFactory.getRequestUpdateCharacter();

            when(characterRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.of(character));
            when(characterRepository.save(any(Character.class))).thenAnswer(returnsFirstArg());
            doNothing().when(kafkaService).sendEvent(any(), any());

            Character updatedCharacter = characterService.update(character.getId(), requestUpdateCharacter);

            assertThat(updatedCharacter.getName()).isEqualTo(requestUpdateCharacter.getName());
            assertThat(updatedCharacter.getDescription()).isEqualTo(requestUpdateCharacter.getDescription());
            assertThat(updatedCharacter.getYear()).isEqualTo(requestUpdateCharacter.getYear());

            verify(characterRepository, times(1)).save(Mockito.any(Character.class));
        }

        @Test
        @DisplayName("Given I have a character When I try to updated a no exists character Then  it should throw a character id not found exception")
        public void shouldNotUpdateCharacterAndExpectedCharacterIdNotFoundException() {
            String id = "invalid_character_id";
            Character requestUpdateCharacter = CharacterFactory.getRequestUpdateCharacter();

            when(characterRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> characterService.update(id, requestUpdateCharacter))
                    .isInstanceOf(CharacterIdNotFoundException.class);
        }
    }

    @Nested
    public class DeleteCharacter {
        @Test
        @DisplayName("Given I have a exists character When I try to delete a character Then a character is deleted")
        public void shouldDeleteCharacter(){
            Character character = CharacterFactory.getCharacter();

            when(characterRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.of(character));
            when(characterRepository.save(any(Character.class))).thenAnswer(returnsFirstArg());
            doNothing().when(kafkaService).sendEvent(any(), any());

            Character deletedCharacter = characterService.delete(character.getId());

            assertThat(deletedCharacter.getActive()).isFalse();

            verify(characterRepository, times(1)).save(Mockito.any(Character.class));
        }

        @Test
        @DisplayName("Given I have a character When I try to deleted a no exists character Then  it should throw a character id not found exception")
        public void shouldNotDeleteCharacterAndExpectedCharacterIdNotFoundException() {
            String id = "invalid_character_id";

            when(characterRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> characterService.delete(id))
                    .isInstanceOf(CharacterIdNotFoundException.class);
        }
    }

    @Nested
    public class GetCharacter {
        @Test
        @DisplayName("Given I have a exists characters When I try to find all by filter Then a character is finded by filter")
        public void shouldFindCharacterByFilter(){
            Character character = CharacterFactory.getCharacter();
            List<Character> characters = List.of(character);

            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Character> page = new PageImpl<Character>(characters, pageRequest, 1);

            when(characterRepository.findAll(any(PageRequest.class))).thenReturn(page);

            Page<Character> result = characterService.getAll(pageRequest);

            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent()).isEqualTo(characters);
            assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
            assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        }

        @Test
        @DisplayName("Given I have a exists characters When I try to find by id Then a character is returned")
        public void shouldGetCharacterByCharacterId(){
            Character character = CharacterFactory.getCharacter();

            when(characterRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.of(character));

            Character existCharacter = characterService.getById(character.getId());

            assertThat(existCharacter.getName()).isEqualTo(character.getName());
            assertThat(existCharacter.getDescription()).isEqualTo(character.getDescription());
            assertThat(existCharacter.getYear()).isEqualTo(character.getYear());

            verify(characterRepository, times(1)).findByIdAndActiveTrue(character.getId());
        }

        @Test
        @DisplayName("Given I have a character When I try to find a no exists character id Then it should throw a character id not found exception")
        public void shouldNotGetCharacterByCharacterAndExpectedCharacterIdNotFoundException() {
            String id = "invalid_character_id";

            when(characterRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> characterService.getById(id))
                    .isInstanceOf(CharacterIdNotFoundException.class);
        }
    }

}
