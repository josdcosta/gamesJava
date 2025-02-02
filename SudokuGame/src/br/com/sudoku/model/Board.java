package br.com.sudoku.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Random;
import static br.com.sudoku.model.GameStatusEnum.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces = new ArrayList<>();

    public Board(int difficult) {
        for (int i = 0; i < 9; i++) {
            List<Space> row = new ArrayList<>(); // Criar uma linha (lista de 9 espaços)
            for (int j = 0; j < 9; j++) {
                Space space = new Space(); // Criar um espaço (celular)
                row.add(space); // Adicionar o espaço à linha
            }
            spaces.add(row); // Adicionar a linha ao tabuleiro
        }
        do {
            clearBoard(); // Reinicia o tabuleiro antes de tentar gerar novamente
        } while (!generateBoard()); // Vai continuar tentando até o tabuleiro ser completamente preenchido

        do {
            fixedPositions(difficult);
        } while (countSolutions() > 1); // Vai continuar tentando até o tabuleiro ser completamente preenchido
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    private boolean generateBoard() {
        List<Integer> validNumbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            validNumbers.add(i);
        }
        int cont = 0;
        for (int l = 0; l < 9; l++) {
            for (int c = 0; c < 9; c++) {
                // Embaralha a lista para randomizar a ordem dos números
                Collections.shuffle(validNumbers);

                // Tenta colocar um número válido na posição
                for (int num : validNumbers) {
                    if (validRow(l, num) && validCol(c, num) && validSubgrid(l, c, num)) {
                        spaces.get(l).get(c).setExpected(num);
                        cont++;
                        break; // Se encontrar um número válido, sai do loop
                    }
                }
            }
        }
        return cont == 81;
    }

    private void clearBoard() {
        for (List<Space> row : spaces) {
            for (Space space : row) {
                space.setExpected(0); // Reinicia cada espaço para um valor padrão (0 ou outro valor que represente um espaço vazio)
            }
        }
    }

    public boolean validCol(int colunaIndex, int value) {
        List<Space> colunas = spaces.stream()
                .map(linha -> linha.get(colunaIndex)) // Pega o elemento da coluna específica de cada linha
                .toList(); // Coleta os elementos em uma lista
        return colunas.stream().noneMatch(c -> c.getExpected() == value);
    }

    public boolean validRow(int linhaIndex, int value) {
        List<Space> linha = spaces.get(linhaIndex); // Pega a linha específica
        return linha.stream().noneMatch(s -> s.getExpected() == value); // Verifica se o valor já existe na linha
    }

    public boolean validSubgrid(int linhaIndex, int colunaIndex, int value) {
        int subgridRowStart = (linhaIndex / 3) * 3;
        int subgridColStart = (colunaIndex / 3) * 3;

        for (int i = subgridRowStart; i < subgridRowStart + 3; i++) {
            for (int j = subgridColStart; j < subgridColStart + 3; j++) {
                if (spaces.get(i).get(j).getExpected() == value) {
                    return false; // Se encontrar o valor na subgrade, retorna false
                }
            }
        }
        return true;
    }

    public GameStatusEnum getStatus(){
        if(spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getValue()))){
            return NON_STARTED;
        };
        return  spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getValue())) ? INCOMPLETE : COMPLETE;
    }

    public boolean hasErrors(){
        if(getStatus() == NON_STARTED){
            return false;
        }

        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getValue()) && !s.getValue().equals(s.getExpected()));
    }

    public void fixedPositions(int num) {
        Random rand = new Random();
        int lin;
        int col;
        int fixedCount = 0;
        clearValue();
        clearFixed();

        while (fixedCount < num) {
            lin = rand.nextInt(9); // Gera um número entre 0 e 8
            col = rand.nextInt(9); // Gera um número entre 0 e 8
            // Verifica se a célula ainda não está fixa e se o valor esperado é diferente de zero
            if (!spaces.get(lin).get(col).isFixed() && spaces.get(lin).get(col).getExpected() != 0) {
                spaces.get(lin).get(col).setValue(spaces.get(lin).get(col).getExpected());
                spaces.get(lin).get(col).setFixed(true);
                fixedCount++;
            }
        }
    }

    public void viewResolution() {
        System.out.println("Solução do Sudoku:");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = spaces.get(j).get(i).getExpected(); // Troquei i e j para inverter as linhas e colunas
                // Adiciona um formato de grid mais claro, com bordas entre os quadrantes 3x3
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(value + " ");
            }
            System.out.println(); // Nova linha para cada linha do tabuleiro
            // Adiciona uma linha separadora entre os quadrantes 3x3
            if ((i + 1) % 3 == 0 && i != 8) {
                System.out.println("---------------------");
            }
        }
    }


    public int countSolutions() {
        return backtrackCount(0, 0);
    }

    private int backtrackCount(int row, int col) {
        // Se chegou ao final do tabuleiro, encontrou uma solução
        if (row == 9) {
            return 1;
        }

        // Se chegou ao final da linha, passa para a próxima linha
        if (col == 9) {
            return backtrackCount(row + 1, 0);
        }

        // Se a célula já está preenchida, passa para a próxima célula
        if (spaces.get(row).get(col).getExpected() != 0) {
            return backtrackCount(row, col + 1);
        }

        int count = 0;

        // Tenta todos os números de 1 a 9
        for (int num = 1; num <= 9; num++) {
            if (validRow(row, num) && validCol(col, num) && validSubgrid(row, col, num)) {
                // Coloca o número na célula
                spaces.get(row).get(col).setExpected(num);

                // Chama recursivamente para a próxima célula
                count += backtrackCount(row, col + 1);

                // Backtracking: remove o número da célula
                spaces.get(row).get(col).setExpected(0);
            }
        }
        return count;
    }

    private void clearFixed() {
        for (List<Space> row : spaces) {
            for (Space space : row) {
                space.setFixed(false); // Reinicia cada espaço para um valor padrão (0 ou outro valor que represente um espaço vazio)
            }
        }
    }

    private void clearValue() {
        for (List<Space> row : spaces) {
            for (Space space : row) {
                space.clearSpace(); // Reinicia cada espaço para um valor padrão (0 ou outro valor que represente um espaço vazio)
            }
        }
    }

    public boolean changeValue(final int col, final int row, final int value){
        var space = this.spaces.get(col).get(row);
        if(space.isFixed()){
            return false;
        }
        space.setValue(value);
        return true;
    }

    public boolean gameIsFinished(){
        return !hasErrors() && getStatus().equals(COMPLETE);
    }

    public void reset(int difficult){
        do {
            clearBoard(); // Reinicia o tabuleiro antes de tentar gerar novamente
        } while (!generateBoard()); // Vai continuar tentando até o tabuleiro ser completamente preenchido

        do {
            fixedPositions(difficult);
        } while (countSolutions() > 1); // Vai continuar tentando até o tabuleiro ser completamente preenchido
    }
}
