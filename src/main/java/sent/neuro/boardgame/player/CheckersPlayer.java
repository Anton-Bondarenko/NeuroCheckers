package sent.neuro.boardgame.player;

import lombok.Getter;

public class CheckersPlayer implements Player {
    @Getter
    private final BlackOrWhite colour;

    public CheckersPlayer(BlackOrWhite colour) {
        this.colour = colour;
    }
}
