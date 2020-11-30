package br.com.marvel.service.comic.services.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "character")
public interface CharacterServiceClient {

    @GetMapping("/v1/public/characters/{id}")
    CharacterDTO getById(@PathVariable String id);

}
