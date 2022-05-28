package sent.neuro.boardgame.figure;

import lombok.Getter;
import lombok.Setter;
import sent.neuro.boardgame.player.BlackOrWhite;

public class CheckerFigure implements Figure {
    @Getter
    private final BlackOrWhite colour;
    @Getter
    @Setter
    private boolean king = false;

    public CheckerFigure(BlackOrWhite colour) {
        this.colour = colour;
    }
}
