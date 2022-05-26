package sent.neuro.boardgame.board;

import sent.neuro.boardgame.figure.Figure;

public interface Cell {
    Figure getFigure();
    void setFigure(Figure figure);
}
