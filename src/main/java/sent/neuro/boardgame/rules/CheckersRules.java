package sent.neuro.boardgame.rules;

import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.board.chess.BlackOrWhite;
import sent.neuro.boardgame.board.chess.ChessBoard;
import sent.neuro.boardgame.game.exceptions.NotAbleToMove;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.CheckersPlayer;
import sent.neuro.boardgame.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckersRules implements Rules {

    @Override
    public boolean isAllowed(Board board, Move move) {
        return true;
    }

    @Override
    public Board initialBoard() {
        return new ChessBoard();
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
    public Player nextMove(Board board, List<Player> players, Player moveHolder) throws NotAbleToMove {
        int countdown = players.size();
        do {
            var nextPlayerIndex = players.indexOf(moveHolder) + 1;
            nextPlayerIndex = (nextPlayerIndex < players.size()) ? nextPlayerIndex : 0;
            moveHolder = players.get(nextPlayerIndex);
            if (countdown-- <= 0)
                throw new NotAbleToMove("No any move available!");
        } while (isAbleToMove(board, moveHolder));
        return moveHolder;
    }

    @Override
    public List<Player> initialPlayers() {
        var players = new ArrayList<Player>(BlackOrWhite.values().length);
        Arrays.stream(BlackOrWhite.values()).forEach(colour -> players.add(new CheckersPlayer(colour)));
        return players;
    }
}
