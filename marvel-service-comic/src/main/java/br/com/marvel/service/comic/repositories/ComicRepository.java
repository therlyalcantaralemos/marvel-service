package br.com.marvel.service.comic.repositories;

import br.com.marvel.service.comic.models.Comic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComicRepository extends MongoRepository<Comic, String>, ComicRepositoryCustom {
    Optional<Comic> findByNumberAndTitleAndActiveTrue(Integer number, String title);

    Optional<Comic> findByIdAndActiveTrue(String id);
}
