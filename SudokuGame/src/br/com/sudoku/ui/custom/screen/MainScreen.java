package br.com.sudoku.ui.custom.screen;

import br.com.sudoku.model.Difficult;
import br.com.sudoku.model.Space;
import br.com.sudoku.services.BoardServices;
import br.com.sudoku.services.EventEnum;
import br.com.sudoku.services.NotifierService;
import br.com.sudoku.ui.custom.buttons.CheckGameStatusButton;
import br.com.sudoku.ui.custom.buttons.DifficultLevelButton;
import br.com.sudoku.ui.custom.buttons.FinishGameButton;
import br.com.sudoku.ui.custom.buttons.ResetButton;
import br.com.sudoku.ui.custom.frame.MainFrame;
import br.com.sudoku.ui.custom.input.NumberText;
import br.com.sudoku.ui.custom.panel.MainPanel;
import br.com.sudoku.ui.custom.panel.SudokuSector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import static br.com.sudoku.model.DifficultEnum.*;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class MainScreen {
    private final BoardServices boardServices;
    private  final NotifierService notifierService;

    private final static Dimension dimension = new Dimension(600, 600);

    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;
    private JButton difficultLevel;


    public MainScreen(){
        this.boardServices = new BoardServices();
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);
        reconstructor(mainPanel);
        addResetButton(mainPanel);
        addShowGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        addDifficultLevel(mainPanel);
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
                boardServices.viewResolution();
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
        resetButton = new ResetButton(e -> {
            var dialogResult = JOptionPane.showConfirmDialog(
                    null,
                    "Deseja reiniciar?",
                    "Limpar o jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
            );
            if (dialogResult == JOptionPane.YES_OPTION) {

                remountPanel(mainPanel, Difficult.getLevel());
            }
        });
        mainPanel.add(resetButton);
    }

    private void addDifficultLevel(JPanel mainPanel) {
        String[] options = {"Muito fácil", "Fácil", "Médio", "Difícil", "Muito difícil"};
        difficultLevel = new DifficultLevelButton(e -> {
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Escolha o nível de dificuldade:",
                    "Configuração do Jogo",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == -1) {
                JOptionPane.showMessageDialog(null, "Nenhuma opção selecionada.");
            }else {
                switch (options[choice]) {
                    case "Muito fácil" -> {
                        remountPanel(mainPanel, VERYEASY.getLevel());
                    }
                    case "Fácil" -> {
                        remountPanel(mainPanel, EASY.getLevel());
                    }
                    case "Médio" -> {
                        remountPanel(mainPanel, MODERATE.getLevel());
                    }
                    case "Difícil" -> {
                        remountPanel(mainPanel, HARD.getLevel());
                    }
                    case "Muito difícil" -> {
                        remountPanel(mainPanel, VERYHARD.getLevel());
                    }
                    default -> JOptionPane.showMessageDialog(null, "Nenhuma opção selecionada.");
                }
            }
        });
        mainPanel.add(difficultLevel);
    }

    private void reconstructor (JPanel mainPanel){
        for (int r = 0; r < 9; r+=3){
            var endRow = r + 2;
            for (int c = 0; c < 9; c+=3) {
                var endCol = c + 2;
                var spaces = getSpacesFromSector(boardServices.getSpaces(), c, endCol, r, endRow);
                JPanel sector = genetateSection(spaces);
                mainPanel.add(sector);
            }
        }
    }

    private void remountPanel(JPanel mainPanel, int DifficultLevel) {
        boardServices.difficultLevel(DifficultLevel);
        boardServices.reset(); // Reset dos dados internos
        mainPanel.removeAll(); // Remove todos os componentes da tela

        // Reconstroi os setores do tabuleiro dentro do mesmo mainPanel
        reconstructor(mainPanel);

        // Adiciona novamente os botões
        addResetButton(mainPanel);
        addShowGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        addDifficultLevel(mainPanel);

        mainPanel.revalidate(); // Atualiza o layout
        mainPanel.repaint(); // Re-renderiza a tela
    }


}
