package sent.neuro.boardgame.controller;

import lombok.NonNull;
import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.board.Position;
import sent.neuro.boardgame.board.chess.ChessBoard;
import sent.neuro.boardgame.figure.CheckerFigure;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.move.checkers.CheckersMove;
import sent.neuro.boardgame.player.Player;

import java.util.Arrays;
import java.util.Scanner;

import static sent.neuro.boardgame.board.chess.BlackOrWhite.BLACK;
import static sent.neuro.boardgame.board.chess.ChessBoard.ROWS_NUM;

public class CheckerConsoleController implements Controller {
    @Override
    public Move yourMove(@NonNull Board board, @NonNull Player player) {
        drawBoard((ChessBoard) board);
        System.out.println("Pick up a figure:");
        var figurePosition = userInputPosition();
        System.out.println("Move direction:");
        var newPosition = userInputPosition();
        var figure = board.getCell(figurePosition).getFigure();
        return new CheckersMove(player, newPosition, figure);
    }

    private void drawBoard(ChessBoard board) {
        for (var row = 1; row <= ROWS_NUM; row++) {
            var rowNum = row;
            Arrays.stream(ChessBoard.FileLetter.values()).forEach(fileLetter -> {
                var position = new ChessBoard.ChessBoardPosition(fileLetter, rowNum);
                var cell = board.getCell(position);
                var figure = (CheckerFigure) cell.getFigure();
                if (figure == null)
                    drawEmptyCell();
                else if (figure.getColour().equals(BLACK))
                    drawBlackChecker();
                else
                    drawWhiteChecker();
            });
        }
    }

    private Position userInputPosition() {
        System.out.println("row then file");
        Scanner scanner = new Scanner(System.in);
        String rowStr = scanner.nextLine();
        String fileStr = scanner.nextLine();
        return new ChessBoard.ChessBoardPosition(ChessBoard.FileLetter.valueOf(fileStr), Integer.parseInt(rowStr));
    }

    private void drawEmptyCell() {
        System.out.print(" ");
    }

    private void drawBlackChecker() {
        System.out.print("B");
    }

    private void drawWhiteChecker() {
        System.out.print("W");
    }
}
