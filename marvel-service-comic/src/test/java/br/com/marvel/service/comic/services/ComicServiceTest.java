package br.com.marvel.service.comic.services;


import br.com.marvel.service.comic.exceptions.ComicAlreadyExistsException;
import br.com.marvel.service.comic.exceptions.ComicIdNotFoundException;
import br.com.marvel.service.comic.factories.CharacterFactory;
import br.com.marvel.service.comic.factories.ComicFactory;
import br.com.marvel.service.comic.models.CharacterItem;
import br.com.marvel.service.comic.models.Comic;
import br.com.marvel.service.comic.repositories.ComicRepository;
import br.com.marvel.service.comic.services.client.CharacterDTO;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ComicServiceTest {

    @InjectMocks
    private ComicService comicService;

    @Mock
    private ComicRepository comicRepository;

    @Mock
    private CharacterService characterService;

    @Nested
    public class CreateComic {
        @Test
        @DisplayName("Given I have a comic data When I try to create a comic Then a comic is created")
        public void shouldCreateComic(){
            Comic requestComic = ComicFactory.getRequestComic();
            CharacterItem characterItem = ComicFactory.getCharacterItem();
            List<String> characters = List.of(characterItem.getId());

            when(comicRepository.findByNumberAndTitleAndActiveTrue(any(Integer.class), any(String.class))).thenReturn(Optional.empty());
            when(characterService.getById(anyString())).thenReturn(characterItem);
            when(comicRepository.save(any(Comic.class))).thenAnswer(returnsFirstArg());

            Comic createdComic = comicService.create(requestComic, characters);

            assertThat(createdComic.getTitle()).isEqualTo(requestComic.getTitle());
            assertThat(createdComic.getNumber()).isEqualTo(requestComic.getNumber());
            assertThat(createdComic.getRelease()).isEqualTo(requestComic.getRelease());
            assertThat(createdComic.getAuthor()).isEqualTo(requestComic.getAuthor());
            assertThat(createdComic.getCharacters()).isEqualTo(createdComic.getCharacters());

            verify(comicRepository, times(1)).save(any(Comic.class));

        }

        @Test
        @DisplayName("Given I have a comic data When I try to create a exists comic Then  it should throw a comic already exists exception")
        public void shouldNotCreatedComicAndExpectedCharacterAlreadyExistsException(){
            Comic requestComic = ComicFactory.getRequestComic();
            Comic comic = ComicFactory.getComic();
            List<String> characters = List.of("123");

            when(comicRepository.findByNumberAndTitleAndActiveTrue(any(Integer.class), any(String.class))).thenReturn(Optional.of(comic));

            assertThatThrownBy( () -> comicService.create(requestComic, characters))
                    .isInstanceOf(ComicAlreadyExistsException.class);

            verify(comicRepository, never()).save(any(Comic.class));
        }
    }


    @Nested
    public class UpdateComic {
        @Test
        @DisplayName("Given I have a exists comic When I try to update a character Then a comic is updated")
        public void shouldUpdateComic(){
            Comic requestComic = ComicFactory.getRequestUpdateComic();
            Comic comic = ComicFactory.getComic();
            CharacterItem characterItem = ComicFactory.getCharacterItem();
            List<String> characters = List.of(characterItem.getId());

            when(comicRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.of(comic));
            when(characterService.getById(anyString())).thenReturn(characterItem);
            when(comicRepository.save(any(Comic.class))).thenAnswer(returnsFirstArg());

            Comic updatedComic = comicService.update(comic.getId(), requestComic, characters);

            assertThat(updatedComic.getTitle()).isEqualTo(requestComic.getTitle());
            assertThat(updatedComic.getNumber()).isEqualTo(requestComic.getNumber());
            assertThat(updatedComic.getRelease()).isEqualTo(requestComic.getRelease());
            assertThat(updatedComic.getAuthor()).isEqualTo(requestComic.getAuthor());
            assertThat(updatedComic.getCharacters()).isEqualTo(requestComic.getCharacters());

            verify(comicRepository, times(1)).save(any(Comic.class));

        }

        @Test
        @DisplayName("Given I have a comic When I try to updated a no exists comic Then  it should throw a comic id not found exception")
        public void shouldNotUpdateCharacterAndExpectedCharacterIdNotFoundException() {
            String id = "invalid_comic_id";
            Comic requestComic = ComicFactory.getRequestUpdateComic();
            List<String> characters = List.of("123");

            when(comicRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> comicService.update(id, requestComic, characters))
                    .isInstanceOf(ComicIdNotFoundException.class);
        }
    }

    @Nested
    public class DeleteComic {
        @Test
        @DisplayName("Given I have a exists comic When I try to delete a comic Then a comic is deleted")
        public void shouldDeleteComic(){
            Comic comic = ComicFactory.getComic();

            when(comicRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.of(comic));
            when(comicRepository.save(any(Comic.class))).thenAnswer(returnsFirstArg());

            Comic deletedComic = comicService.delete(comic.getId());

            assertThat(deletedComic.getActive()).isFalse();

            verify(comicRepository, times(1)).save(Mockito.any(Comic.class));
        }

        @Test
        @DisplayName("Given I have a comic When I try to deleted a no exists comic Then it should throw a comic id not found exception")
        public void shouldNotDeleteComicAndExpectedComicIdNotFoundException() {
            String id = "invalid_comic_id";

            when(comicRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> comicService.delete(id))
                    .isInstanceOf(ComicIdNotFoundException.class);
        }
    }

    @Nested
    public class GetCharacter {
        @Test
        @DisplayName("Given I have a exists comic When I try to find all by filter Then a comic is finded by filter")
        public void shouldFindComicByFilter(){
            Comic comic = ComicFactory.getComic();
            List<Comic> comics = List.of(comic);

            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Comic> page = new PageImpl<Comic>(comics, pageRequest, 1);

            when(comicRepository.findAll(any(PageRequest.class))).thenReturn(page);

            Page<Comic> result = comicService.getAll(pageRequest);

            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent()).isEqualTo(comics);
            assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
            assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        }

        @Test
        @DisplayName("Given I have a exists comic When I try to find by id Then a comic is returned")
        public void shouldGetComicByComicId(){
            Comic comic = ComicFactory.getComic();

            when(comicRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.of(comic));

            Comic existComic = comicService.getById(comic.getId());

            assertThat(existComic.getTitle()).isEqualTo(comic.getTitle());
            assertThat(existComic.getNumber()).isEqualTo(comic.getNumber());
            assertThat(existComic.getRelease()).isEqualTo(comic.getRelease());
            assertThat(existComic.getAuthor()).isEqualTo(comic.getAuthor());

            verify(comicRepository, times(1)).findByIdAndActiveTrue(comic.getId());
        }

        @Test
        @DisplayName("Given I have a comic When I try to find a no exists comic id Then it should throw a comic id not found exception")
        public void shouldNotGetComicByComicAndExpectedComicIdNotFoundException() {
            String id = "invalid_comic_id";

            when(comicRepository.findByIdAndActiveTrue(any(String.class))).thenReturn(Optional.empty());

            assertThatThrownBy(() -> comicService.getById(id))
                    .isInstanceOf(ComicIdNotFoundException.class);
        }
    }
}
