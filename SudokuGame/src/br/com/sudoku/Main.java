package br.com.sudoku;

import br.com.sudoku.model.Board;
import br.com.sudoku.model.Difficult;
import br.com.sudoku.ui.custom.screen.MainScreen;

import java.util.Scanner;

import static br.com.sudoku.model.Difficult.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        var mainScreen = new MainScreen();
        mainScreen.buildMainScreen();

    }
}