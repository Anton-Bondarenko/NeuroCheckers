package sent.neuro.boardgame.controller;

import sent.neuro.boardgame.board.Board;
import sent.neuro.boardgame.move.Move;
import sent.neuro.boardgame.player.Player;

public interface Controller {
    Move yourMove(Board board, Player player);
    void errorHandler(Throwable error);
}
