package br.com.marvel.service.comic.event;

public interface Event {
    default String getEventType(){
        return this.getClass().getSimpleName();
    }
}
