package sent.neuro.boardgame.gui;

import lombok.NonNull;
import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.board.chess.ChessBoard;
import sent.neuro.boardgame.board.exceptions.DoNotBreakBoardException;
import sent.neuro.boardgame.figure.CheckerFigure;
import sent.neuro.boardgame.player.BlackOrWhite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BoardPanel extends JPanel {



    private static final int BRD_LEFT_BORDER = 24;
    private static final int BRD_BOTTOM_BORDER = 31;
    private static final int BRD_FULL_HEIGHT = 288;
    private static final int BRD_FULL_WIDTH = 280;
    private static final int BRD_AREA_WIDTH = BRD_FULL_WIDTH - BRD_LEFT_BORDER;
    private static final int BRD_AREA_HEIGHT = BRD_FULL_HEIGHT - BRD_BOTTOM_BORDER;

    private static final int BRD_H_SHIFT_CELL = 8;
    private static final int BRD_V_SHIFT_CELL = 24;

    private static final String BOARD_IMG_RES = "img/Knights-board.png";
    private BufferedImage boardImage = null;

    private Board board = null;

    @Override
    protected void paintComponent(Graphics g) {
        drawBoardImg(g);
        if (board != null)
            drawState(g, board);
    }

    private void drawBoardImg(Graphics g){
        if (boardImage == null) {
            var imgFile = getClass().getClassLoader().getResource(BOARD_IMG_RES);
            if (imgFile == null)
                throw new DoNotBreakBoardException("board image not found");
            try {
                boardImage = ImageIO.read(imgFile);
            } catch (IOException e) {
                throw new DoNotBreakBoardException("board image not found");
            }
        }
        g.drawImage(boardImage, 0, 0, null);
    }

    private void drawFigure(Graphics g, @NonNull CheckerFigure figure, @NonNull ChessBoard.ChessBoardPosition position) {
        if (figure.getColour().equals(BlackOrWhite.WHITE)) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.GRAY);
        }
        var cellWidth = BRD_AREA_WIDTH / ChessBoard.COLS_NUM;
        var cellHeight = BRD_AREA_HEIGHT / ChessBoard.ROWS_NUM;
        var realX = BRD_LEFT_BORDER + cellWidth * position.getFileInd() + BRD_H_SHIFT_CELL;
        var realY = BRD_FULL_HEIGHT - (BRD_BOTTOM_BORDER + (cellHeight * (position.getRankInd()))) - BRD_V_SHIFT_CELL;
        g.fillOval(realX, realY, cellWidth / 2, cellHeight / 2);
        g.setColor(Color.BLACK);
        g.drawOval(realX, realY, cellWidth / 2, cellHeight / 2);
    }

    private void drawState(Graphics g, Board board) {
        for (var rankInd = 0; rankInd < ChessBoard.ROWS_NUM; rankInd++) {
            for (var fileInd = 0; fileInd < ChessBoard.COLS_NUM; fileInd++) {
                var position = new ChessBoard.ChessBoardPosition(fileInd, rankInd);
                var figure = (CheckerFigure) board.getCell(position).getFigure();
                if (figure != null) drawFigure(g, figure, position);
            }
        }
    }

    public void setState(Board board){
        this.board = board;
        drawBoardImg(getGraphics());
        drawState(getGraphics(), board);
    }
}
