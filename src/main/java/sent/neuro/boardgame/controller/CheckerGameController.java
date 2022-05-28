package sent.neuro.boardgame.controller;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.board.chess.ChessBoard;
import sent.neuro.boardgame.board.exceptions.DoNotBreakBoardException;
import sent.neuro.boardgame.figure.CheckerFigure;
import sent.neuro.boardgame.figure.Figure;
import sent.neuro.boardgame.game.exceptions.NotAbleToMove;
import sent.neuro.boardgame.game.exceptions.PlayException;
import sent.neuro.boardgame.game.exceptions.WrongMove;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.CheckersPlayer;
import sent.neuro.boardgame.player.Player;
import sent.neuro.boardgame.rules.Rules;

import java.util.List;

import static sent.neuro.boardgame.rules.checkers.CheckersUtils.getAvailableEatVariants;
import static sent.neuro.boardgame.rules.checkers.CheckersUtils.getEatenFigure;

@Slf4j
public class CheckerGameController implements GameController {
    @Getter
    private final Rules rules;
    private List<Player> players;
    @Getter
    private Board board;
    @Getter
    private boolean gameOver = true;

    public CheckerGameController(Rules rules) {
        this.rules = rules;
    }

    @Override
    public void errorHandler(Throwable error) {
        log.error(error.getMessage());
    }

    @Override
    public Player startNewGame() {
        this.players = rules.initialPlayers();
        this.board = rules.initialBoard();
        gameOver = false;
        return players.get(0);
    }

    public Player nextMove(@NonNull Move move) throws PlayException {
        if (gameOver)
            throw new NotAbleToMove("Game is over, folks");
        var moveHolder =  (CheckersPlayer)move.getPlayer();
        if (!rules.isAllowed(board, move, moveHolder))
            throw new WrongMove("Such move is not allowed");

        Figure eatenFigure = getEatenFigure((ChessBoard) board, move);
        try {
            board.moveFigure(move.getFigure(), move.getNewPosition());
            if (eatenFigure != null) {
                board.removeFigure(((ChessBoard) board).getFigurePosition(eatenFigure));
                // eat chain
                if (!getAvailableEatVariants((ChessBoard) board, (CheckerFigure)move.getFigure()).isEmpty())
                    moveHolder.setOnlyFigureAllowed(eatenFigure);
            }
        } catch (DoNotBreakBoardException e) {
            throw new NotAbleToMove("Mess on the board!");
        }
        gameOver = rules.isWinner(board, move.getPlayer());
        try {
            if (eatenFigure == null) {
                moveHolder.setOnlyFigureAllowed(null);
                moveHolder = (CheckersPlayer) rules.nextPlayer(board, players, move.getPlayer());
            }
        } catch (NotAbleToMove e) {
            errorHandler(e);
            gameOver = true;
        }
        return moveHolder;
    }
}
