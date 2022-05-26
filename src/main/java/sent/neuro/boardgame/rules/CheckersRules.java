package sent.neuro.boardgame.rules;

import lombok.extern.slf4j.Slf4j;
import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.board.chess.ChessBoard;
import sent.neuro.boardgame.board.exceptions.DoNotBreakBoardException;
import sent.neuro.boardgame.figure.CheckerFigure;
import sent.neuro.boardgame.game.exceptions.NotAbleToMove;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.BlackOrWhite;
import sent.neuro.boardgame.player.CheckersPlayer;
import sent.neuro.boardgame.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sent.neuro.boardgame.board.chess.ChessBoard.FileLetter.*;
import static sent.neuro.boardgame.player.BlackOrWhite.BLACK;
import static sent.neuro.boardgame.player.BlackOrWhite.WHITE;

@Slf4j
public class CheckersRules implements Rules {
    @Override
    public boolean isAllowed(Board board, Move move) {
        return true;
    }

    @Override
    public Board initialBoard() {
        var board = new ChessBoard();
        try {
            //
            // white set
            //
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(A, 1));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(C, 1));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(E, 1));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(G, 1));

            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(B, 2));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(D, 2));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(F, 2));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(H, 2));

            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(A, 3));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(C, 3));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(E, 3));
            board.addFigure(new CheckerFigure(WHITE), new ChessBoard.ChessBoardPosition(G, 3));

            //
            // black set
            //
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(B, 8));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(D, 8));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(F, 8));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(H, 8));

            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(A, 7));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(C, 7));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(E, 7));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(G, 7));

            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(B, 6));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(D, 6));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(F, 6));
            board.addFigure(new CheckerFigure(BLACK), new ChessBoard.ChessBoardPosition(H, 6));
        } catch (DoNotBreakBoardException e) {
            log.error("Wrong initial board setting. Fuck up here.");
        }

        return board;
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
