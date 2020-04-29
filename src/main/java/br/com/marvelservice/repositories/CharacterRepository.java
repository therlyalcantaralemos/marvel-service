package br.com.marvelservice.repositories;

import br.com.marvelservice.models.Character;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends MongoRepository<Character, String>, CharacterRepositoryCustom {
    Optional<Character> findByNameAndActiveTrue(String name);

    Optional<Character> findByIdAndActiveTrue(String id);

    List<Character> findByIdInAndActiveTrue(List<String> id);
}
