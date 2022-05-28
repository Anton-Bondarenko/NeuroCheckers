package sent.neuro.boardgame.board.chess;

import lombok.EqualsAndHashCode;
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
    public Cell getCell(Position position){
        var chessPosition = (ChessBoardPosition) position;
        if (chessPosition.getFileInd() < 0 || chessPosition.getFileInd() >= COLS_NUM
                || chessPosition.getRankInd() < 0 || chessPosition.getRankInd() >= ROWS_NUM)
            throw new DoNotBreakBoardException("Out of Board!");
        return rows.get(chessPosition.getRankInd()).get(((ChessBoardPosition) position).getFileInd());
    }

    @Override
    public List<Cell> getCells() {
        var cells = new ArrayList<Cell>(ROWS_NUM * COLS_NUM);
        rows.forEach(cells::addAll);
        return cells;
    }

    @Override
    public Figure getFigure(Position position) {
        return getCell(position).getFigure();
    }

    @Override
    public void moveFigure(Figure figure, Position newPosition){
        var newCell = (ChessCell) getCell(newPosition);
        cellShouldBeEmpty(newCell);
        var oldCell = getFigureCell(figure);
        cellShouldNotBeEmpty(oldCell);

        newCell.setFigure(figure);
        oldCell.setFigure(null);
    }

    @Override
    public void addFigure(@NonNull Figure figure, Position position){
        var cell = (ChessCell) getCell(position);
        cellShouldBeEmpty(cell);
        cell.setFigure(figure);
    }

    @Override
    public void removeFigure(Position position){
        var cell = (ChessCell) getCell(position);
        cellShouldNotBeEmpty(cell);
        cell.setFigure(null);
    }

    @Override
    public Cell getFigureCell(Figure figure){
        return getCells().stream().filter(cell -> cell.getFigure() == figure).findFirst().orElseThrow(() -> new DoNotBreakBoardException("This figure is not in the game"));
    }

    @Override
    public Position getCellPosition(Cell cell){
        for(var row = 0; row < ROWS_NUM; row ++) {
            var cRows = rows.get(row);
            for (var col = 0; col < COLS_NUM; col++) {
            if (cRows.get(col) == cell)
                return new ChessBoardPosition(col, row);
            }
        }
        throw new DoNotBreakBoardException("Chess board does not have such cell");
    }

    private void cellShouldBeEmpty(Cell cell){
        if (cell.getFigure() != null) throw new DoNotBreakBoardException("Cannot put figure here, it is not empty!");
    }

    private void cellShouldNotBeEmpty(Cell cell){
        if (cell.getFigure() == null) throw new DoNotBreakBoardException("Cannot get figure, cell is empty!");
    }

    public Position getFigurePosition(Figure figure){
        return getCellPosition(getFigureCell(figure));
    }

    public boolean isEmptyCell(Position position){
        return getCell(position).getFigure() == null;
    }

    public enum FileLetter {
        A, B, C, D, E, F, G, H;

        public int getFileIndex() {
            return this.ordinal();
        }
    }

    public static class ChessCell implements Cell {
        @Getter
        Position position;
        @Getter
        @Setter
        private Figure figure = null;
    }

    @EqualsAndHashCode
    public static class ChessBoardPosition implements Position {
        @Getter
        private final int fileInd;
        @Getter
        private final int rankInd;
        public ChessBoardPosition(int fileInd, int rankInd) {
            this.fileInd = fileInd;
            this.rankInd = rankInd;
        }
    }
}
