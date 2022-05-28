package sent.neuro.boardgame.rules;

import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.game.exceptions.NotAbleToMove;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.Player;

import java.util.List;

public interface Rules {

    Board initialBoard();

    boolean isAllowed(Board board, Move move, Player player);

    boolean isWinner(Board board, Player player);

    boolean isAbleToMove(Board board, Player player);

    Player nextPlayer(Board board, List<Player> players, Player moveHolder) throws NotAbleToMove;

    List<Player> initialPlayers();
}
