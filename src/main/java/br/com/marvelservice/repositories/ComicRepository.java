package br.com.marvelservice.repositories;

import br.com.marvelservice.models.Comic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComicRepository extends MongoRepository<Comic, String> {
    Optional<Comic> findByNumberAndTitleAndActiveTrue(Integer number, String title);

    Optional<Comic> findByIdAndActiveTrue(String id);
}
