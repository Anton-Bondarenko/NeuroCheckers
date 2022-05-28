package sent.neuro.boardgame.player;

import lombok.Getter;
import lombok.Setter;
import sent.neuro.boardgame.figure.Figure;

public class CheckersPlayer implements Player {
    @Getter
    private final BlackOrWhite colour;
    @Getter
    @Setter
    private Figure onlyFigureAllowed = null;
    @Getter
    private int eaten = 0;
    public void didEat(){eaten ++;}

    public CheckersPlayer(BlackOrWhite colour) {
        this.colour = colour;
    }
}
