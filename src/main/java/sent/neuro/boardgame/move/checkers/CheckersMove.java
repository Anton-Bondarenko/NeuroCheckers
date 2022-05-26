package sent.neuro.boardgame.move.checkers;

import lombok.Value;
import sent.neuro.boardgame.board.Position;
import sent.neuro.boardgame.figure.Figure;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.Player;

@Value
public class CheckersMove implements Move {
    Player player;
    Position newPosition;
    Figure figure;
}
