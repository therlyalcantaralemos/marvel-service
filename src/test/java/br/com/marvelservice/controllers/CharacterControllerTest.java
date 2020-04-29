package br.com.marvelservice.controllers;

import br.com.marvelservice.controllers.dto.CreateUpdateCharacterDTO;
import br.com.marvelservice.factories.CharacterFactory;
import br.com.marvelservice.models.Character;
import br.com.marvelservice.services.CharacterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.data.mongodb.host=0", "spring.data.mongodb.port=0", "spring.mongodb.embedded.version=4.0.2"}
)
@AutoConfigureMockMvc
public class CharacterControllerTest {

    private final String API_CHARACTER = "/v1/public/characters";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CharacterService characterService;

    @Test
    @DisplayName("Given I have a character data When I try to create a character Then a character is created")
    public void shouldCreateCharacter() throws Exception {
        CreateUpdateCharacterDTO createUpdateCharacterDTO = CharacterFactory.getCreateUpdateCharacterDTO();
        Character character = CharacterFactory.getCharacter();

        given(characterService.create(Mockito.any(Character.class))).willReturn(character);

        MockHttpServletRequestBuilder request = post(API_CHARACTER)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUpdateCharacterDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(character.getId()))
                .andExpect(jsonPath("$.name").value(createUpdateCharacterDTO.getName()))
                .andExpect(jsonPath("$.description").value(createUpdateCharacterDTO.getDescription()))
                .andExpect(jsonPath("$.year").value(createUpdateCharacterDTO.getYear()));

    }

    @Test
    @DisplayName("Given I have a exists character  When I try to update a character Then a character is updated")
    public void  shouldUpdateCharacter() throws Exception {
        CreateUpdateCharacterDTO createUpdateCharacterDTO = CharacterFactory.getCreateUpdateCharacterDTO();
        createUpdateCharacterDTO.setName("spider man update");
        Character character = CharacterFactory.getCharacter();
        character.setName("spider man update");

        given(characterService.update(any(String.class), any(Character.class))).willReturn(character);

        MockHttpServletRequestBuilder request = put(API_CHARACTER + "/{id}", character.getId())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUpdateCharacterDTO));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(character.getId()))
                .andExpect(jsonPath("$.name").value(character.getName()))
                .andExpect(jsonPath("$.description").value(character.getDescription()))
                .andExpect(jsonPath("$.year").value(character.getYear()));

    }

    @Test
    @DisplayName("Given I have a exists character When I try to delete a character Then a character is deleted")
    public void  shouldDeleteCharacter() throws Exception {
        String id = "1";

        MockHttpServletRequestBuilder request = delete(API_CHARACTER + "/{id}", id)
                .accept(APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(characterService, times(1)).delete(id);
    }

    @Test
    @DisplayName("Given I have a exists characters When I try to find all by filter Then a character is returned")
    public void shouldFindCharacterByFilter() throws Exception {
        Character character = CharacterFactory.getCharacter();

        given(characterService.getAll(any(Pageable.class)))
                .willReturn(new PageImpl<Character>(Collections.singletonList(character), PageRequest.of(0, 100), 1));

        String queryString = String.format("?name=%s&description=%s&page=0&size=100", character.getName(), character.getDescription());

        MockHttpServletRequestBuilder request = get(API_CHARACTER + queryString)
                .accept(APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("total_elements").value(1))
                .andExpect(jsonPath("pageable.page_size").value(100))
                .andExpect(jsonPath("pageable.page_number").value(0));
    }

    @Test
    @DisplayName("Given I have a exists characters When I try to find by id Then a character is returned")
    public void shouldGetCharacterById() throws Exception {
        Character character = CharacterFactory.getCharacter();

        given(characterService.getById(any(String.class))).willReturn(character);

        MockHttpServletRequestBuilder request = get(API_CHARACTER + "/{id}", character.getId())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(character.getId()))
                .andExpect(jsonPath("$.name").value(character.getName()))
                .andExpect(jsonPath("$.description").value(character.getDescription()))
                .andExpect(jsonPath("$.year").value(character.getYear()));

    }

}
