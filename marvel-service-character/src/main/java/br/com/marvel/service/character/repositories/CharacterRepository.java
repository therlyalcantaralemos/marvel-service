package br.com.marvel.service.character.repositories;

import br.com.marvel.service.character.models.Character;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends MongoRepository<Character, String> {

    Optional<Character> findByNameAndActiveTrue(String name);

    Optional<Character> findByIdAndActiveTrue(String id);
}
