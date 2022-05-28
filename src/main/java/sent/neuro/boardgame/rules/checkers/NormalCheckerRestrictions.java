package sent.neuro.boardgame.rules.checkers;

import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.board.chess.ChessBoard;
import sent.neuro.boardgame.board.exceptions.DoNotBreakBoardException;
import sent.neuro.boardgame.figure.CheckerFigure;
import sent.neuro.boardgame.game.exceptions.WrongMove;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.CheckersPlayer;
import sent.neuro.boardgame.player.Player;

import static sent.neuro.boardgame.rules.checkers.CheckersUtils.*;

public class NormalCheckerRestrictions {
    private NormalCheckerRestrictions() {
    }

    public static void simpleMove(ChessBoard board, Move move) throws WrongMove {
        var figure = (CheckerFigure) move.getFigure();
        var newPosition = (ChessBoard.ChessBoardPosition) move.getNewPosition();

        for (var position : getAllAvailableMoves(board, figure)) {
            if (newPosition.equals(position))
                return;
        }
        throw new WrongMove("Move is not available");
    }

    public static void eatMove(ChessBoard board, Move move) throws WrongMove {
        if (getEatenFigure(board, move) == null)
            throw new WrongMove("Wrong eat move");
    }


    public static void commonChecks(Move move, CheckersPlayer player) throws WrongMove {
        var figure = (CheckerFigure) move.getFigure();
        var newPosition = (ChessBoard.ChessBoardPosition) move.getNewPosition();

        if (figure == null)
            throw new WrongMove("Empty cell");

        // should move self figures
        if (!player.getColour().equals(figure.getColour()))
            throw new WrongMove("Do not touch. It is not yours!");

        // check board bounds
        if (newPosition.getRankInd() >= ChessBoard.ROWS_NUM ||
                newPosition.getRankInd() < 0 ||
                newPosition.getFileInd() >= ChessBoard.COLS_NUM ||
                newPosition.getFileInd() < 0)
            throw new WrongMove("Out of board");

        // not a king
        if (figure.isKing())
            throw new DoNotBreakBoardException("This checks not for a king");
    }

    public static void allChecks(Board board, Move move, Player player) throws WrongMove {
        var checkersPlayer = (CheckersPlayer) player;
        var chessBoard = (ChessBoard) board;

        commonChecks(move, checkersPlayer);
        if (isCanPlayerEat(chessBoard, checkersPlayer))
            eatMove(chessBoard, move);
        else
            simpleMove(chessBoard, move);
    }
}
