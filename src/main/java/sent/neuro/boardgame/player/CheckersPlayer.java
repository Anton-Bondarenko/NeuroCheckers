package sent.neuro.boardgame.player;

import lombok.Getter;
import sent.neuro.boardgame.board.chess.BlackOrWhite;

public class CheckersPlayer implements Player {
    @Getter
    private final BlackOrWhite colour;

    public CheckersPlayer(BlackOrWhite colour) {
        this.colour = colour;
    }
}
