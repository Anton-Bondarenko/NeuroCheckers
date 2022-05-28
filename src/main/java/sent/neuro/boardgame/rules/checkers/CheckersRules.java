package sent.neuro.boardgame.rules.checkers;

import lombok.extern.slf4j.Slf4j;
import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.board.chess.ChessBoard;
import sent.neuro.boardgame.board.exceptions.DoNotBreakBoardException;
import sent.neuro.boardgame.figure.CheckerFigure;
import sent.neuro.boardgame.game.exceptions.NotAbleToMove;
import sent.neuro.boardgame.game.exceptions.WrongMove;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.BlackOrWhite;
import sent.neuro.boardgame.player.CheckersPlayer;
import sent.neuro.boardgame.player.Player;
import sent.neuro.boardgame.rules.Rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sent.neuro.boardgame.player.BlackOrWhite.BLACK;
import static sent.neuro.boardgame.player.BlackOrWhite.WHITE;

@Slf4j
public class CheckersRules implements Rules {
    private final NormalCheckerRestrictions checkerRestrictions = new NormalCheckerRestrictions();

    @Override
    public Board initialBoard() {
        var board = new ChessBoard();
        try {
            //
            // white setup
            //
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(0, 0));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(2, 0));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(4, 0));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(6, 0));

            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(1, 1));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(3, 1));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(5, 1));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(7, 1));

            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(0, 2));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(2, 2));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(4, 2));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(6, 2));

            //
            // black setup
            //
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(1, 7));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(3, 7));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(5, 7));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(7, 7));

            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(0, 6));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(2, 6));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(4, 6));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(6, 6));

            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(1, 5));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(3, 5));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(5, 5));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(7, 5));
        } catch (DoNotBreakBoardException e) {
            log.error("Wrong initial board setting. Fuck up here.");
        }

        return board;
    }

    @Override
    public boolean isAllowed(Board board, Move move, Player player) {
        var error = false;
        try {
            checkerRestrictions.allChecks(board, move, player);
        } catch (WrongMove e) {
            log.warn(e.getMessage());
            error = true;
        }
        return !error;
    }

    @Override
    public boolean isWinner(Board board, Player player) {
        return false;
    }

    @Override
    public boolean isAbleToMove(Board board, Player player) {
        return true;
    }

    @Override
    public Player nextPlayer(Board board, List<Player> players, Player moveHolder) throws NotAbleToMove {
        int countdown = players.size();
        do {
            var nextPlayerIndex = players.indexOf(moveHolder) + 1;
            nextPlayerIndex = (nextPlayerIndex < players.size()) ? nextPlayerIndex : 0;
            moveHolder = players.get(nextPlayerIndex);
            if (countdown-- <= 0)
                throw new NotAbleToMove("No any move available!");
        } while (!isAbleToMove(board, moveHolder));
        return moveHolder;
    }

    @Override
    public List<Player> initialPlayers() {
        var players = new ArrayList<Player>(BlackOrWhite.values().length);
        Arrays.stream(BlackOrWhite.values()).forEach(colour -> players.add(new CheckersPlayer(colour)));
        return players;
    }
}
