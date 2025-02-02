package br.com.minesweeper.cm.model;

public interface FieldObserver {
    public void eventTrigger(Field field, FieldEventEnum event);
}
