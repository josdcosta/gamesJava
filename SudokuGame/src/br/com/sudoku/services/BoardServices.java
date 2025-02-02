package br.com.sudoku.services;

import br.com.sudoku.model.Board;
import br.com.sudoku.model.Difficult;
import br.com.sudoku.model.GameStatusEnum;
import br.com.sudoku.model.Space;

import java.util.List;

public class BoardServices {

    private final Board board;

    public BoardServices() {
        this.board = new Board(Difficult.MODERATE.getLevel());
    }

    public List<List<Space>> getSpaces(){
        return this.board.getSpaces();
    }

    public void reset(){
        this.board.reset(Difficult.MODERATE.getLevel());
    }

    public boolean hasErrors(){
        return this.board.hasErrors();
    }

    public GameStatusEnum getStatus(){
        return this.board.getStatus();
    }

    public boolean gameIsFinished(){
        return this.board.gameIsFinished();
    }

}
