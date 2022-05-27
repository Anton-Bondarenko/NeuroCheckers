package sent.neuro.boardgame.rules.checkers;

import sent.neuro.boardgame.board.chess.ChessBoard;
import sent.neuro.boardgame.board.exceptions.DoNotBreakBoardException;
import sent.neuro.boardgame.figure.CheckerFigure;
import sent.neuro.boardgame.game.exceptions.WrongMove;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.BlackOrWhite;

public class NormalCheckerRestrictions {

    public void simpleMove(ChessBoard board, Move move) throws DoNotBreakBoardException, WrongMove {
        var figure = (CheckerFigure) move.getFigure();
        var oldPosition = (ChessBoard.ChessBoardPosition) board.getFigurePosition(figure);
        var newPosition = (ChessBoard.ChessBoardPosition) move.getNewPosition();

        // should do diagonal
        if (Math.abs(oldPosition.getFileInd() - newPosition.getFileInd()) != 1)
            throw new WrongMove("Move diagonally");

        // should move one row forward
        if (((figure.getColour().equals(BlackOrWhite.WHITE)) &&
                (newPosition.getRankInd() - oldPosition.getRankInd() != 1)) ||
                ((figure.getColour().equals(BlackOrWhite.BLACK)) &&
                        (newPosition.getRankInd() - oldPosition.getRankInd() != -1)))
            throw new WrongMove("Forward one step move only allowed");

        // destination should be empty
        if (!board.isEmptyCell(newPosition))
            throw new WrongMove("Destination is not empty");
    }

    public void eatMove(ChessBoard board, Move move) throws DoNotBreakBoardException, WrongMove {
        var figure = (CheckerFigure) move.getFigure();
        var oldPosition = (ChessBoard.ChessBoardPosition) board.getFigurePosition(figure);
        var newPosition = (ChessBoard.ChessBoardPosition) move.getNewPosition();

        // should do diagonal
        if (Math.abs(oldPosition.getFileInd() - newPosition.getRankInd()) != 2)
            throw new WrongMove("Move diagonally");

        // should be opponent figure
        var jumpFileInd = newPosition.getFileInd() + (newPosition.getFileInd() - oldPosition.getFileInd())/2;
        var jumpRankInd = newPosition.getRankInd() + (newPosition.getRankInd() - oldPosition.getRankInd())/2;
        board.getCell(new ChessBoard.ChessBoardPosition(jumpFileInd, jumpRankInd));

        // should move one row forward
        if (((figure.getColour().equals(BlackOrWhite.WHITE)) &&
                (newPosition.getRankInd() - oldPosition.getRankInd() != 1)) ||
                ((figure.getColour().equals(BlackOrWhite.BLACK)) &&
                        (newPosition.getRankInd() - oldPosition.getRankInd() != -1)))
            throw new WrongMove("Forward one step move only allowed");
    }


    public void allChecks(ChessBoard board, Move move) throws DoNotBreakBoardException, WrongMove {

    }
}
