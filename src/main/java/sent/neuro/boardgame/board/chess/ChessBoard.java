package sent.neuro.boardgame.board.chess;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.board.Cell;
import sent.neuro.boardgame.board.Position;
import sent.neuro.boardgame.board.exceptions.DoNotBreakBoardException;
import sent.neuro.boardgame.figure.Figure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChessBoard implements Board {
    public static final int ROWS_NUM = 8;
    public static final int COLS_NUM = 8;

    private final List<List<ChessCell>> rows;

    public ChessBoard() {
        rows = Stream.generate(ArrayList<ChessCell>::new).limit(ROWS_NUM).collect(Collectors.toList());
        rows.forEach(files -> files.addAll(Stream.generate(ChessCell::new).limit(COLS_NUM).collect(Collectors.toList())));
    }

    @Override
    public Cell getCell(Position position) {
        var chessPosition = (ChessBoardPosition) position;
        return rows.get(chessPosition.getRank() - 1).get(((ChessBoardPosition) position).getFileLetter().getFileNumber());
    }

    @Override
    public List<Cell> getCells() {
        var cells = new ArrayList<Cell>(ROWS_NUM * COLS_NUM);
        rows.forEach(cells::addAll);
        return cells;
    }

    @Override
    public Figure touchFigure(Position position) {
        return getCell(position).getFigure();
    }

    @Override
    public void moveFigure(Figure figure, Position newPosition) throws DoNotBreakBoardException {
        var newCell = (ChessCell) getCell(newPosition);
        cellShouldBeEmpty(newCell);
        var oldCell = whereAmI(figure);
        cellShouldNotBeEmpty(oldCell);

        newCell.setFigure(figure);
        oldCell.setFigure(null);
    }

    @Override
    public void addFigure(@NonNull Figure figure, Position position) throws DoNotBreakBoardException {
        var cell = (ChessCell) getCell(position);
        cellShouldBeEmpty(cell);
        cell.setFigure(figure);
    }

    @Override
    public void removeFigure(Position position) throws DoNotBreakBoardException {
        var cell = (ChessCell) getCell(position);
        cellShouldNotBeEmpty(cell);
        cell.setFigure(null);
    }

    @Override
    public Cell whereAmI(Figure figure) throws DoNotBreakBoardException {
        return getCells().stream().filter(cell -> cell.getFigure() == figure).findFirst().orElseThrow(() -> new DoNotBreakBoardException("This figure is not in the game"));
    }

    private void cellShouldBeEmpty(Cell cell) throws DoNotBreakBoardException {
        if (cell.getFigure() != null) throw new DoNotBreakBoardException("Cannot put figure here, it is not empty!");
    }

    private void cellShouldNotBeEmpty(Cell cell) throws DoNotBreakBoardException {
        if (cell.getFigure() == null) throw new DoNotBreakBoardException("Cannot get figure, cell is empty!");
    }

    public enum FileLetter {
        A, B, C, D, E, F, G, H;

        public int getFileNumber() {
            return this.ordinal();
        }
    }

    public static class ChessCell implements Cell {
        @Getter
        @Setter
        private Figure figure = null;
    }

    public static class ChessBoardPosition implements Position {
        @Getter
        private final FileLetter fileLetter;
        @Getter
        private final int rank;
        public ChessBoardPosition(FileLetter fileLetter, int rank) {
            this.fileLetter = fileLetter;
            this.rank = rank;
        }
    }
}
