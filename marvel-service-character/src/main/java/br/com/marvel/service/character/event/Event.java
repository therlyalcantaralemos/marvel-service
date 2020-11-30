package br.com.marvel.service.character.event;

public interface Event {
    default String getEventType(){
        return this.getClass().getSimpleName();
    }
}
