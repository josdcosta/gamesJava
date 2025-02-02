package br.com.sudoku.ui.custom.input;

import br.com.sudoku.model.Space;
import br.com.sudoku.services.EventEnum;
import br.com.sudoku.services.EventListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class NumberText extends JTextField implements EventListener {
    private Space space;
    public NumberText(final Space space){
        this.space = space;
        var dimension = new Dimension(50, 50);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setFont(new Font("Arial", Font.PLAIN, 20));
        this.setHorizontalAlignment(CENTER);
        this.setDocument(new NumberTextLimit());
        this.setEnabled(!space.isFixed());
        if(space.isFixed()){
            this.setText(space.getValue().toString());
        }
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changeSpace();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
            private void changeSpace(){
                if(getText().isEmpty()){
                    space.clearSpace();
                    return;
                }
                space.setValue(Integer.parseInt(getText()));
            }
        });
    }

    @Override
    public void update(EventEnum eventType) {
        if(eventType.equals(EventEnum.CLEAR_SPACE) && (this.isEnabled())){
            this.setText("");
        }
    }
}
