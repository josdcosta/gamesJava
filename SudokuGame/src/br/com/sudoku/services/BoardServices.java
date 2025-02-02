package br.com.sudoku.services;

import br.com.sudoku.model.Board;
import br.com.sudoku.model.Difficult;
import br.com.sudoku.model.Space;
import br.com.sudoku.model.GameStatusEnum;

import java.util.List;

public class BoardServices {

    private final Board board;

    public BoardServices() {
        this.board = new Board(Difficult.getLevel());
    }

    public List<List<Space>> getSpaces(){
        return this.board.getSpaces();
    }

    public void reset(){
        this.board.reset(Difficult.getLevel());
    }

    public void difficultLevel(int level){
        Difficult.setLevel(level);
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

    public void viewResolution(){
        this.board.viewResolution();
    }

}
