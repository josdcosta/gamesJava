package br.com.minesweeper.cm.button;

import br.com.minesweeper.cm.model.Field;
import br.com.minesweeper.cm.model.FieldEventEnum;
import br.com.minesweeper.cm.model.FieldObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ButtonField extends JButton implements FieldObserver, MouseListener {

    private final Color BG_DEFAULF = new Color(184, 184, 184);
    private final Color BG_CHECKED= new Color(8, 179, 247);
    private final Color BG_EXPLOSION = new Color(189, 66, 68);
    private final Color TEXT_GREEN = new Color(0, 100, 0);
    private final Field field;

    public ButtonField(Field field) {
            this.field = field;
            setBackground(BG_DEFAULF);
            setOpaque(true);
            setBorder(BorderFactory.createBevelBorder(0));
            addMouseListener(this);
            field.subscribe(this);
    }

    @Override
    public void eventTrigger(Field field, FieldEventEnum event) {
        switch (event){
            case OPEN ->{
                applyStyleOpen();
            }
            case CHECK -> {
                applyStyleCheck();
            }
            case EXPLOSION -> {
                applyStyleExplosion();
            }
            default -> {
                applyStyleDefault();
            }
        }
        SwingUtilities.invokeLater(() -> {
            repaint();
            validate();
        });
    }

    private void applyStyleDefault() {
        setBackground(BG_DEFAULF);
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(0));
        setText("");
    }

    private void applyStyleExplosion() {
        setBackground(BG_EXPLOSION);
        setForeground(Color.WHITE);
        setText("X");
    }

    private void applyStyleCheck() {
        setBackground(BG_CHECKED);
        setForeground(Color.BLACK);
        setText("M");
    }

    private void applyStyleOpen() {
        if(field.isMined()){
            setBackground(BG_EXPLOSION);
            setForeground(Color.GRAY);
            setText("X");
            return;
        }
        setBackground(BG_DEFAULF);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        switch (field.minesInTheNighborhood()){
            case 1: {
                setForeground(TEXT_GREEN);
                break;
            }
            case 2: {
                setForeground(Color.BLUE);
                break;
            }
            case 3 : {
                setForeground(Color.YELLOW);
                break;
            }
            case 4:
            case 5:
            case 6: {
                setForeground(Color.RED);
                break;
            }
            default:{
                setForeground(Color.PINK);
            }
        }
        String value = !field.neighborhoodSafe()?
                field.minesInTheNighborhood() + "" :
                "";
        setText(value);
    }

    //Interface dos eventos do mouse

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == 1){
            field.open();
        } else {
            field.toggleCheck();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
