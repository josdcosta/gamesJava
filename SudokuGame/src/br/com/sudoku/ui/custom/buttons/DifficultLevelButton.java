package br.com.sudoku.ui.custom.buttons;

import javax.swing.JButton;
import java.awt.event.ActionListener;

public class DifficultLevelButton extends JButton {

    public DifficultLevelButton(final ActionListener actionListener){
        this.setText("Dificuldade");
        this.addActionListener(actionListener);
    }

}
