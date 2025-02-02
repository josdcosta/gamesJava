package br.com.minesweeper.cm.view;

import br.com.minesweeper.cm.button.ButtonField;
import br.com.minesweeper.cm.model.Board;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    public BoardPanel(Board board) {
        setLayout(new GridLayout(board.getRow(), board.getCol()));
        int total = board.getRow()*board.getCol();
        board.forEachField(c -> add(new ButtonField(c)));
        board.subscribe(e -> {
            SwingUtilities.invokeLater( () -> {
                    if(e.isWin()){
                        JOptionPane.showMessageDialog(this, "Você ganhou!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Você perdeu!");
                    }
                    board.restart();
                }
            );
        });
    }
}
