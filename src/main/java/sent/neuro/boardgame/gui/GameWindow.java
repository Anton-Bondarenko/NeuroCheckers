package sent.neuro.boardgame.gui;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.board.Position;
import sent.neuro.boardgame.board.chess.ChessBoard;
import sent.neuro.boardgame.controller.Controller;
import sent.neuro.boardgame.figure.CheckerFigure;
import sent.neuro.boardgame.game.exceptions.PlayException;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.move.checkers.CheckersMove;
import sent.neuro.boardgame.player.BlackOrWhite;
import sent.neuro.boardgame.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class GameWindow extends JFrame {
    private static final String BOARD_IMG_RES = "img/Knights-board.png";

    private static final int WND_WIDTH = 400;
    private static final int WND_HEIGHT = 400;

    private static final int BRD_LEFT_BORDER = 24;
    private static final int BRD_BOTTOM_BORDER = 31;
    private static final int BRD_FULL_HEIGHT = 288;
    private static final int BRD_FULL_WIDTH = 280;
    private static final int BRD_AREA_WIDTH = BRD_FULL_WIDTH - BRD_LEFT_BORDER;
    private static final int BRD_AREA_HEIGHT = BRD_FULL_HEIGHT - BRD_BOTTOM_BORDER;

    private static final int BRD_H_SHIFT_CELL = 8;
    private static final int BRD_V_SHIFT_CELL = 24;


    private final Controller controller;
    private JPanel contentPane;
    private JButton moveButton;
    private JButton newGameButton;
    private JPanel actionPane;
    private JPanel boardPane;
    private JTextField fromFileInput;
    private JTextField toFileInput;
    private JTextField fromRankInput;
    private JTextField toRankInput;
    private JLabel fromFileLbl;
    private JLabel fromRankLbl;
    private JLabel toFileLbl;
    private JLabel toRankLbl;

    private Player moveHolder;

    public GameWindow(Controller controller) {
        this.controller = controller;
        newGameButton.addActionListener(event -> startNewGame());
        moveButton.addActionListener(event -> doMove(getMove()));
    }

    private Graphics2D getBoardGraphics() {
        return (Graphics2D) boardPane.getGraphics();
    }

    private void drawBoard() {
        try {
            var imgFile = getClass().getClassLoader().getResource(BOARD_IMG_RES);
            if (imgFile == null)
                throw new IOException("board image not found");
            var boardImage = ImageIO.read(imgFile);
            getBoardGraphics().drawImage(boardImage, 0, 0, null);
        } catch (IOException e) {
            log.error("Cannot load board image", e);
        }
    }

    private void drawState(Board board) {
        for (var rank = 1; rank <= ChessBoard.ROWS_NUM; rank++) {
            int finalRank = rank;
            Arrays.stream(ChessBoard.FileLetter.values()).forEach(file -> {
                var position = new ChessBoard.ChessBoardPosition(file, finalRank);
                var figure = (CheckerFigure) board.getCell(position).getFigure();
                if (figure != null) drawFigure(figure, position);
            });
        }
    }

    private void drawFigure(@NonNull CheckerFigure figure, @NonNull ChessBoard.ChessBoardPosition position) {
        var g = getBoardGraphics();
        if (figure.getColour().equals(BlackOrWhite.WHITE)) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.GRAY);
        }
        var cellWidth = BRD_AREA_WIDTH / ChessBoard.COLS_NUM;
        var cellHeight = BRD_AREA_HEIGHT / ChessBoard.ROWS_NUM;
        var realX = BRD_LEFT_BORDER + cellWidth * position.getFileLetter().getFileNumber() + BRD_H_SHIFT_CELL;
        var realY = BRD_FULL_HEIGHT - (BRD_BOTTOM_BORDER + (cellHeight * (position.getRank() - 1))) - BRD_V_SHIFT_CELL;
        g.fillOval(realX, realY, cellWidth / 2, cellHeight / 2);
        g.setColor(Color.BLACK);
        g.drawOval(realX, realY, cellWidth / 2, cellHeight / 2);
    }

    public void drawAll(Board board){
        drawBoard();
        drawState(board);
    }

    public void showGameWindow() {
        setTitle("Game window");
        setPreferredSize(new Dimension(WND_WIDTH, WND_HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(contentPane);
        setResizable(false);
        pack();
        setVisible(true);
        drawBoard();
    }

    public void startNewGame() {
        moveHolder = controller.startNewGame();
        drawAll(controller.getBoard());
    }

    private Position getFigurePosition(){
        return new ChessBoard.ChessBoardPosition(
                ChessBoard.FileLetter.valueOf(fromFileInput.getText().toUpperCase()),
                Integer.parseInt(fromRankInput.getText())
        );
    }

    private Position getNewPosition(){
        return new ChessBoard.ChessBoardPosition(
                ChessBoard.FileLetter.valueOf(toFileInput.getText().toUpperCase()),
                Integer.parseInt(toRankInput.getText())
        );
    }

    private Move getMove(){
        return new CheckersMove(moveHolder, getNewPosition(),
                controller.getBoard().getCell(getFigurePosition()).getFigure());
    }

    public void doMove(Move move){
        try {
            moveHolder = controller.nextMove(move);
            drawAll(controller.getBoard());
        } catch (PlayException e) {
            log.error("Wrong move: {}", e.getMessage());
        }
    }

}
