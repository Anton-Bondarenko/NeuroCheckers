package sent.neuro.boardgame.board;

import sent.neuro.boardgame.board.exceptions.DoNotBreakBoardException;
import sent.neuro.boardgame.figure.Figure;

public interface Board {
    Cell getCell(Position position);
    Iterable<Cell> getCells();
    Figure touchFigure(Position position);
    void moveFigure(Figure figure, Position newPosition) throws DoNotBreakBoardException;
    void addFigure(Figure figure, Position position) throws DoNotBreakBoardException;
    void removeFigure(Position position) throws DoNotBreakBoardException;
    Cell whereAmI(Figure figure) throws DoNotBreakBoardException;
}
