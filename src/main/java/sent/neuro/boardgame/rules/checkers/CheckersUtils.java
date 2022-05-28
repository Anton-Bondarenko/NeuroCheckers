package sent.neuro.boardgame.rules.checkers;

import sent.neuro.boardgame.board.chess.ChessBoard;
import sent.neuro.boardgame.board.exceptions.DoNotBreakBoardException;
import sent.neuro.boardgame.figure.CheckerFigure;
import sent.neuro.boardgame.figure.Figure;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.BlackOrWhite;
import sent.neuro.boardgame.player.CheckersPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckersUtils {
    public static final String NOT_SUPPORTED = "Not yet supported";

    private CheckersUtils() {
    }

    public static List<ChessBoard.ChessBoardPosition> getForwardDiagonals(ChessBoard board, CheckerFigure figure) {
        var positionsAvailable = new ArrayList<ChessBoard.ChessBoardPosition>(2);
        var position = (ChessBoard.ChessBoardPosition) board.getFigurePosition(figure);

        if (figure.isKing())
            throw new DoNotBreakBoardException(NOT_SUPPORTED);

        var rankDirection = figure.getColour().equals(BlackOrWhite.WHITE) ? 1 : -1;
        if (position.getFileInd() > 0 && position.getRankInd() < ChessBoard.ROWS_NUM - 1
                && position.getRankInd() + rankDirection > 0 && position.getRankInd() + rankDirection < ChessBoard.COLS_NUM) {
            positionsAvailable.add(new ChessBoard.ChessBoardPosition(position.getFileInd() - 1, position.getRankInd() + rankDirection));
        }

        if (position.getFileInd() < ChessBoard.COLS_NUM - 1 && position.getRankInd() < ChessBoard.ROWS_NUM - 1
                && position.getRankInd() + rankDirection > 0 && position.getRankInd() + rankDirection < ChessBoard.COLS_NUM) {
            positionsAvailable.add(new ChessBoard.ChessBoardPosition(position.getFileInd() + 1, position.getRankInd() + rankDirection));
        }

        return positionsAvailable;
    }

    public static List<ChessBoard.ChessBoardPosition> getBackwardDiagonals(ChessBoard board, CheckerFigure figure) {
        var positionsAvailable = new ArrayList<ChessBoard.ChessBoardPosition>(2);
        var position = (ChessBoard.ChessBoardPosition) board.getFigurePosition(figure);
        var rankDirection = figure.getColour().equals(BlackOrWhite.WHITE) ? -1 : 1;

        if (figure.isKing())
            throw new DoNotBreakBoardException(NOT_SUPPORTED);

        if (position.getFileInd() > 0 && position.getRankInd() > 0
                && position.getRankInd() + rankDirection > 0 && position.getRankInd() + rankDirection < ChessBoard.COLS_NUM) {
            positionsAvailable.add(new ChessBoard.ChessBoardPosition(position.getFileInd() - 1,
                    position.getRankInd() + rankDirection));
        }

        if (position.getFileInd() < ChessBoard.COLS_NUM - 1 && position.getRankInd() > 0
                && position.getRankInd() + rankDirection > 0 && position.getRankInd() + rankDirection < ChessBoard.COLS_NUM) {
            positionsAvailable.add(new ChessBoard.ChessBoardPosition(position.getFileInd() + 1,
                    position.getRankInd() + rankDirection));
        }

        return positionsAvailable;
    }


    public static List<ChessBoard.ChessBoardPosition> getAvailableDiagonals(ChessBoard board, CheckerFigure figure) {
        var positionsAvailable = new ArrayList<ChessBoard.ChessBoardPosition>(4);
        positionsAvailable.addAll(getForwardDiagonals(board, figure));
        positionsAvailable.addAll(getBackwardDiagonals(board, figure));
        return positionsAvailable;
    }

    public static boolean isCanEat(ChessBoard board, CheckerFigure playerFigure, CheckerFigure opponentFigure) {
        if (playerFigure.isKing())
            throw new DoNotBreakBoardException(NOT_SUPPORTED);

        var playerPosition = (ChessBoard.ChessBoardPosition) board.getFigurePosition(playerFigure);
        var opponentPosition = (ChessBoard.ChessBoardPosition) board.getFigurePosition(opponentFigure);

        // should stay close
        if (Math.abs(playerPosition.getRankInd() - opponentPosition.getRankInd()) != 1 &&
                Math.abs(playerPosition.getFileInd() - opponentPosition.getFileInd()) != 1)
            return false;

        // should have different colours
        if (playerFigure.getColour().equals(opponentFigure.getColour()))
            return false;

        // should have empty space to jump
        var rankDir = (opponentPosition.getRankInd() - playerPosition.getRankInd()) * 2;
        var fileDir = (opponentPosition.getFileInd() - playerPosition.getFileInd()) * 2;

        var newRankInd = playerPosition.getRankInd() + rankDir;
        var newFileInd = playerPosition.getFileInd() + fileDir;
        // check if out of board
        if (newRankInd < 0 || newRankInd >= ChessBoard.COLS_NUM || newFileInd < 0 || newFileInd >= ChessBoard.ROWS_NUM)
            return false;

        return board.getCell(new ChessBoard.ChessBoardPosition(newFileInd, newRankInd)).getFigure() == null;
    }

    public static List<CheckerFigure> getAllPlayersFigures(ChessBoard board, CheckersPlayer player) {
        return board.getCells().stream()
                .filter(cell -> cell.getFigure() != null && ((CheckerFigure) cell.getFigure()).getColour().equals(player.getColour()))
                .map(cell -> (CheckerFigure) cell.getFigure()).collect(Collectors.toList());
    }

    public static List<ChessBoard.ChessBoardPosition> getAllAvailableMoves(ChessBoard board, CheckerFigure figure) {
        if (figure.isKing())
            throw new DoNotBreakBoardException(NOT_SUPPORTED);
        return getForwardDiagonals(board, figure).stream().filter(board::isEmptyCell)
                .collect(Collectors.toList());
    }

    // Opponent figures to eat
    public static List<CheckerFigure> getAvailableEatVariants(ChessBoard board, CheckerFigure figure) {
        return
                // available directions
                CheckersUtils.getAvailableDiagonals(board, figure).stream()
                        // find non empty cells around
                        .filter(position -> !board.isEmptyCell(position))
                        // check if it is opponent and can be jumped over
                        .filter(position -> CheckersUtils.isCanEat(board, figure, (CheckerFigure) board.getFigure(position)))
                        .map(position -> (CheckerFigure) board.getFigure(position))
                        .collect(Collectors.toList());
    }

    public static boolean isCanPlayerEat(ChessBoard board, CheckersPlayer player) {
        var playersFigures = CheckersUtils.getAllPlayersFigures(board, player);
        return playersFigures.stream().anyMatch(figure ->
                !CheckersUtils.getAvailableEatVariants(board, figure).isEmpty());
    }

    public static Figure getEatenFigure(ChessBoard board, Move move) {
        var figure = (CheckerFigure) move.getFigure();
        var oldPosition = (ChessBoard.ChessBoardPosition) board.getFigurePosition(figure);
        var newPosition = (ChessBoard.ChessBoardPosition) move.getNewPosition();

        for (var opponentFigure : getAvailableEatVariants(board, figure)) {
            var opponentPosition = (ChessBoard.ChessBoardPosition) board.getFigurePosition(opponentFigure);
            var fileDirection = opponentPosition.getFileInd() - oldPosition.getFileInd();
            var rankDirection = opponentPosition.getRankInd() - oldPosition.getRankInd();
            if (newPosition.equals(
                    new ChessBoard.ChessBoardPosition(
                            oldPosition.getFileInd() + fileDirection * 2,
                            oldPosition.getRankInd() + rankDirection * 2)))
                return opponentFigure;
        }
        return null;
    }
}
