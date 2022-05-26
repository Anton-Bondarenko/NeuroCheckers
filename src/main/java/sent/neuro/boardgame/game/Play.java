package sent.neuro.boardgame.game;

import lombok.NonNull;
import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.board.exceptions.DoNotBreakBoardException;
import sent.neuro.boardgame.controller.Controller;
import sent.neuro.boardgame.game.exceptions.NotAbleToMove;
import sent.neuro.boardgame.game.exceptions.PlayException;
import sent.neuro.boardgame.game.exceptions.WrongMove;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.Player;
import sent.neuro.boardgame.rules.Rules;

import java.util.List;

public class Play {
    private final Rules rules;
    private List<Player> players;
    private Board board;
    private Player moveHolder;
    private final Controller controller;

    public Play(@NonNull Rules rules, @NonNull Controller controller) {
        this.controller = controller;
        this.rules = rules;
        newGame();
    }

    public void newGame() {
        players = rules.initialPlayers();
        board = rules.initialBoard();
        moveHolder = players.get(0);
    }

    private void doMove(Move move) throws PlayException {
        if (!rules.isAllowed(board, move))
            throw new WrongMove("Such move is not allowed");
        try {
            board.moveFigure(move.getFigure(), move.getNewPosition());
        } catch (DoNotBreakBoardException e) {
            throw new NotAbleToMove("Mess on the board!");
        }
    }

    public void playGame() {
        var isGameOver = false;
        do {
            var move = controller.yourMove(board, moveHolder);
            try {
                doMove(move);
            } catch (PlayException e) {
                controller.errorHandler(e);
            }
            isGameOver = rules.isWinner(board, moveHolder);
            try {
                moveHolder = rules.nextPlayer(board, players, moveHolder);
            } catch (NotAbleToMove e) {
                controller.errorHandler(e);
                isGameOver = true;
            }
        } while (!isGameOver);
    }
}
