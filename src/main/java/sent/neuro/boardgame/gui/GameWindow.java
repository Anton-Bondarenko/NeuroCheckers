package sent.neuro.boardgame.gui;

import lombok.extern.slf4j.Slf4j;
import sent.neuro.boardgame.board.Position;
import sent.neuro.boardgame.board.chess.ChessBoard;
import sent.neuro.boardgame.controller.GameController;
import sent.neuro.boardgame.game.exceptions.PlayException;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.move.checkers.CheckersMove;
import sent.neuro.boardgame.player.CheckersPlayer;
import sent.neuro.boardgame.player.Player;

import javax.swing.*;
import java.awt.*;

@Slf4j
@SuppressWarnings("unused")
public class GameWindow extends JFrame {

    private static final int WND_WIDTH = 450;
    private static final int WND_HEIGHT = 400;


    private final GameController gameController;
    private JPanel contentPane;
    private JButton moveButton;
    private JButton newGameButton;
    private JPanel actionPane;
    private BoardPanel boardPane;
    private JTextField fromInput;
    private JTextField toInput;
    private JLabel fromLbl;
    private JLabel toLbl;
    private JLabel playerLbl;
    private JLabel errorLbl;
    private JLabel inputLbl;

    private Player moveHolder;

    public GameWindow(GameController gameController) {
        this.gameController = gameController;
        newGameButton.addActionListener(event -> startNewGame());
        moveButton.addActionListener(event -> doMove(getMove()));
        errorLbl.setPreferredSize(new Dimension(100, 20));
    }

    private Graphics2D getBoardGraphics() {
        return (Graphics2D) boardPane.getGraphics();
    }

    private void drawInfo(CheckersPlayer player) {
        playerLbl.setText(player.getColour().toString());
    }

    private void showMessage(String message) {
        errorLbl.setText(message);
    }

    private void clearMessage() {
        errorLbl.setText("");
    }

    private void clearMoveField(){
        fromInput.setText("");
        toInput.setText("");
    }

    public void drawAll(GameController gameController) {
        boardPane.setState(gameController.getBoard());
        drawInfo((CheckersPlayer) moveHolder);
    }

    public void showGameWindow() {
        setTitle("Game window");
        setPreferredSize(new Dimension(WND_WIDTH, WND_HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(contentPane);
        setResizable(false);
        pack();
        setVisible(true);
    }

    public void startNewGame() {
        moveHolder = gameController.startNewGame();
        drawAll(gameController);
    }

    private Position getFigurePosition() {
        var fileInd = ChessBoard.FileLetter.valueOf(fromInput.getText().substring(0, 1).toUpperCase()).getFileIndex();
        var rankInd = Integer.parseInt(fromInput.getText().substring(1, 2)) - 1;
        return new ChessBoard.ChessBoardPosition(fileInd, rankInd);
    }

    private Position getNewPosition() {
        var fileInd = ChessBoard.FileLetter.valueOf(toInput.getText().substring(0, 1).toUpperCase()).getFileIndex();
        var rankInd = Integer.parseInt(toInput.getText().substring(1, 2)) - 1;
        return new ChessBoard.ChessBoardPosition(fileInd, rankInd);
    }

    private Move getMove() {
        return new CheckersMove(moveHolder, getNewPosition(),
                gameController.getBoard().getCell(getFigurePosition()).getFigure());
    }

    public void doMove(Move move) {
        try {
            moveHolder = gameController.nextMove(move);
            drawAll(gameController);
            clearMessage();
            clearMoveField();
        } catch (PlayException e) {
            showMessage(e.getMessage());
            log.error("Wrong move: {}", e.getMessage());
        }
        if (gameController.isGameOver()){
            showMessage("Game Over!");
            log.info("Game Over");
        }
    }
}
