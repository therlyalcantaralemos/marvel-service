package br.com.marvelservice.repositories;

import br.com.marvelservice.models.Character;
import br.com.marvelservice.models.ComicItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public interface CharacterRepositoryCustom {

    void updateComic(ComicItem comicItem);

}

class CharacterRepositoryCustomImpl implements CharacterRepositoryCustom{

    private MongoTemplate mongoTemplate;

    @Autowired
    CharacterRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void updateComic(ComicItem comicItem) {
        Update update = new Update()
                .set("comics.number", comicItem.getNumber())
                .set("comics.title", comicItem.getTitle())
                .set("comics.description", comicItem.getDescription())
                .set("comics.author", comicItem.getAuthor())
                .set("comics.release", comicItem.getRelease());

        mongoTemplate.updateFirst( new Query(where("comics.id").is(comicItem.getId())), update, Character.class);

    }
}