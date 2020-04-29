package br.com.marvelservice.controllers;

import br.com.marvelservice.controllers.dto.CreateUpdateComicDTO;
import br.com.marvelservice.factories.ComicFactory;
import br.com.marvelservice.models.Comic;
import br.com.marvelservice.services.ComicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.data.mongodb.host=0", "spring.data.mongodb.port=0", "spring.mongodb.embedded.version=4.0.2"}
)
@AutoConfigureMockMvc
public class ComicControllerTest {

    private final String API_COMIC = "/v1/public/comic";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ComicService comicService;

    @Test
    @DisplayName("Given I have a comic data When I try to create a comic Then a comic is created")
    public void shouldCreateComic() throws Exception {
        CreateUpdateComicDTO createUpdateComicDTO = ComicFactory.getCreateUpdateComicDTO();
        Comic comic = ComicFactory.getComic();

        given(comicService.create(any(Comic.class), anyList())).willReturn(comic);

        MockHttpServletRequestBuilder request = post(API_COMIC)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUpdateComicDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(comic.getId()))
                .andExpect(jsonPath("$.number").value(createUpdateComicDTO.getNumber()))
                .andExpect(jsonPath("$.title").value(createUpdateComicDTO.getTitle()))
                .andExpect(jsonPath("$.characters[0].id").value(comic.getCharacters().get(0).getId()))
                .andExpect(jsonPath("$.author[0]").value(createUpdateComicDTO.getAuthor().get(0)));
    }

    @Test
    @DisplayName("Given I have a exists comic  When I try to update a comic Then a comic is updated")
    public void  shouldUpdateComic() throws Exception {
        CreateUpdateComicDTO createUpdateComicDTO = ComicFactory.getCreateUpdateComicDTO();
        createUpdateComicDTO.setTitle("spider man update title");
        Comic comic = ComicFactory.getComic();
        comic.setTitle("spider man update title");

        given(comicService.update(any(String.class), any(Comic.class), anyList())).willReturn(comic);

        MockHttpServletRequestBuilder request = put(API_COMIC + "/{id}", comic.getId())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUpdateComicDTO));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comic.getId()))
                .andExpect(jsonPath("$.number").value(createUpdateComicDTO.getNumber()))
                .andExpect(jsonPath("$.title").value(createUpdateComicDTO.getTitle()))
                .andExpect(jsonPath("$.characters[0].id").value(comic.getCharacters().get(0).getId()))
                .andExpect(jsonPath("$.author[0]").value(createUpdateComicDTO.getAuthor().get(0)));

    }

    @Test
    @DisplayName("Given I have a exists comic When I try to delete a comic Then a comic is deleted")
    public void  shouldDeleteComic() throws Exception {
        String id = "1";

        MockHttpServletRequestBuilder request = delete(API_COMIC + "/{id}", id)
                .accept(APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(comicService, times(1)).delete(id);
    }

    @Test
    @DisplayName("Given I have a exists comic When I try to find all by filter Then a comic is returned")
    public void shouldFindComicByFilter() throws Exception {
        Comic comic = ComicFactory.getComic();

        given(comicService.getAll(any(Pageable.class)))
                .willReturn(new PageImpl<Comic>(Collections.singletonList(comic), PageRequest.of(0, 100), 1));

        String queryString = String.format("?title=%s&description=%s&page=0&size=100", comic.getTitle(), comic.getDescription());

        MockHttpServletRequestBuilder request = get(API_COMIC + queryString)
                .accept(APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("total_elements").value(1))
                .andExpect(jsonPath("pageable.page_size").value(100))
                .andExpect(jsonPath("pageable.page_number").value(0));
    }

    @Test
    @DisplayName("Given I have a exists comic When I try to find by id Then a comic is returned")
    public void shouldGetComicById() throws Exception {
        Comic comic = ComicFactory.getComic();

        given(comicService.getById(any(String.class))).willReturn(comic);

        MockHttpServletRequestBuilder request = get(API_COMIC + "/{id}", comic.getId())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comic.getId()))
                .andExpect(jsonPath("$.number").value(comic.getNumber()))
                .andExpect(jsonPath("$.title").value(comic.getTitle()))
                .andExpect(jsonPath("$.characters[0].id").value(comic.getCharacters().get(0).getId()))
                .andExpect(jsonPath("$.author[0]").value(comic.getAuthor().get(0)));
    }
}
