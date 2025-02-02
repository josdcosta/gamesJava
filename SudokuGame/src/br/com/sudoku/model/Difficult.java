package br.com.sudoku.model;

public enum Difficult {
    VERYHARD(18),   // Nível 5
    HARD(20),       // Nível 4
    MODERATE(25),   // Nível 3
    EASY(30),       // Nível 2
    VERYEASY(35);   // Nível 1

    private final int level; // Atributo para armazenar o número associado

    // Construtor do enum
    Difficult(int level) {
        this.level = level;
    }

    // Método para retornar o número associado
    public int getLevel() {
        return level;
    }
}
