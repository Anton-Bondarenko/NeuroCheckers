package sent.neuro.boardgame.rules;

import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.game.exceptions.NotAbleToMove;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.Player;

import java.util.List;

public interface Rules {
    boolean isAllowed(Board board, Move move);

    Board initialBoard();

    boolean isWinner(Board board, Player player);

    boolean isAbleToMove(Board board, Player player);

    Player nextMove(Board board, List<Player> players, Player moveHolder) throws NotAbleToMove;

    List<Player> initialPlayers();
}
