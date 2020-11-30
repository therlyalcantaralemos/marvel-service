package br.com.marvel.service.comic.repositories;

import br.com.marvel.service.comic.models.Comic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public interface ComicRepositoryCustom {

    void updateCharacterItemInAllComics(String characterId, String name);

    void removeCharacterItemInAllComics(String characterId);
}

class ComicRepositoryCustomImpl implements ComicRepositoryCustom{

    private final MongoTemplate mongoTemplate;

    @Autowired
    ComicRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void updateCharacterItemInAllComics(String characterId, String name) {
        Update update = new Update();
        update.set("characters.$.name", name);
        Query query = Query.query(Criteria.where("characters.id").is(characterId));
        mongoTemplate.updateMulti(query, update, Comic.class);
    }

    @Override
    public void removeCharacterItemInAllComics(String characterId) {
        Update update = new Update();
        update.pull("characters", Query.query(Criteria.where("id").is(characterId)));
        this.mongoTemplate.updateMulti(new Query(), update, Comic.class);
    }
}