package br.com.sudoku.ui.custom.screen;

import br.com.sudoku.model.Space;
import br.com.sudoku.services.BoardServices;
import br.com.sudoku.services.EventEnum;
import br.com.sudoku.services.NotifierService;
import br.com.sudoku.ui.custom.buttons.FinishGameButton;
import br.com.sudoku.ui.custom.buttons.CheckGameStatusButton;
import br.com.sudoku.ui.custom.buttons.ResetButton;
import br.com.sudoku.ui.custom.frame.MainFrame;
import br.com.sudoku.ui.custom.input.NumberText;
import br.com.sudoku.ui.custom.panel.MainPanel;
import br.com.sudoku.ui.custom.panel.SudokuSector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class MainScreen {
    private final BoardServices boardServices;
    private  final NotifierService notifierService;

    private final static Dimension dimension = new Dimension(600, 600);

    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;


    public MainScreen(){
        this.boardServices = new BoardServices();
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        for (int r = 0; r < 9; r+=3){
            var endRow = r + 2;
            for (int c = 0; c < 9; c+=3) {
                var endCol = c + 2;
                var spaces = getSpacesFromSector(boardServices.getSpaces(), c, endCol, r, endRow);
                JPanel sector = genetateSection(spaces);
                mainPanel.add(sector);
            }
        }
        addResetButton(mainPanel);
        addShowGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private JPanel genetateSection(final List<Space> spaces){
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscribe(EventEnum.CLEAR_SPACE, t));
        return new SudokuSector(fields);
    };

    private List<Space> getSpacesFromSector(final List<List<Space>> spaces,
                                            final int initCol, final int endCol,
                                            final int initRow, final int endRow){
        List<Space> spaceSector = new ArrayList<>();
        for (int r = initRow; r <= endRow; r++) {
            for (int c = initCol; c <= endCol; c++) {
                spaceSector.add(spaces.get(c).get(r));
            }
        }
        return spaceSector;
    }

    private void addFinishGameButton(JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e -> {
            if(boardServices.gameIsFinished()){
                JOptionPane.showMessageDialog(null, "Parabéns, você venceu!");
                resetButton.setEnabled(false);
                finishGameButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "O jogo contem inconsistências!");
            }
        });
        mainPanel.add(finishGameButton);
    }

    private void addShowGameStatusButton(JPanel mainPanel) {
        checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors = boardServices.hasErrors();
            var gameStatus = boardServices.getStatus();
            var message = switch (gameStatus){
                case NON_STARTED -> "O jogo não foi iniciado";
                case INCOMPLETE -> "O jogo está incompleto";
                case COMPLETE -> "O jogo está completo";
            };
            message += hasErrors? " e contem erros":" e não contém erros";
            JOptionPane.showMessageDialog(null, message);
        });
        mainPanel.add(checkGameStatusButton);
    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e ->{
            var dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "Dejesa reiniciar?",
                    "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if(dialogResult == 0){
                boardServices.reset();
                notifierService.notify(EventEnum.CLEAR_SPACE);
            }
        });
        mainPanel.add(resetButton);
    }

}
