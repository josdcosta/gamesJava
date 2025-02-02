package br.com.sudoku.model;

public enum DifficultEnum {

    VERYEASY(35),
    EASY(30),
    MODERATE(25),
    HARD(20),
    VERYHARD(18);

    private final int level; // Campo para armazenar o valor

    DifficultEnum(int level) {
        this.level = level;
    }

    public int getLevel() { // Método para retornar o valor
        return level;
    }
}
