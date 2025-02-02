package br.com.sudoku.ui.custom.buttons;

import javax.swing.JButton;
import java.awt.event.ActionListener;

public class ResetButton extends JButton {
    public ResetButton(final ActionListener actionListener){
        this.setText("Resetar jogo");
        this.addActionListener(actionListener);
    }
}
