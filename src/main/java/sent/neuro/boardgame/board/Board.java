package sent.neuro.boardgame.board;

import sent.neuro.boardgame.figure.Figure;

public interface Board {
    Cell getCell(Position position);
    Iterable<Cell> getCells();
    Figure getFigure(Position position);
    void moveFigure(Figure figure, Position newPosition);
    void addFigure(Figure figure, Position position);
    void removeFigure(Position position);
    Cell getFigureCell(Figure figure);
    Position getCellPosition(Cell cell);
}
