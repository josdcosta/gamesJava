package br.com.sudoku.model;

public class Difficult {

    private static int level = 35; // Atributo para armazenar o número associado

    // Construtor do enum
    Difficult(int level) {
        Difficult.level = level;
    }

    // Método para retornar o número associado
    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        Difficult.level = level;
    }
}
