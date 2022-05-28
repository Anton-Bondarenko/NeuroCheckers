package sent.neuro.boardgame.controller;

import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.game.exceptions.PlayException;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.Player;
import sent.neuro.boardgame.rules.Rules;

public interface GameController {

    Board getBoard();
    Rules getRules();

    boolean isGameOver();

    Player startNewGame();

    Player nextMove(Move move) throws PlayException;

    void errorHandler(Throwable error);

}
