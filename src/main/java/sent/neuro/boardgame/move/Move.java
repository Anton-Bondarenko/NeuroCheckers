package sent.neuro.boardgame.move;

import sent.neuro.boardgame.board.Position;
import sent.neuro.boardgame.figure.Figure;
import sent.neuro.boardgame.player.Player;

public interface Move {
    Player getPlayer();
    Position getNewPosition();
    Figure getFigure();
}
