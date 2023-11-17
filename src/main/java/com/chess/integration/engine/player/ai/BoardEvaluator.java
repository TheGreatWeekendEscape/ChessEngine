package com.chess.integration.engine.player.ai;

import com.chess.integration.engine.board.Board;

public interface BoardEvaluator {

    int evaluate(Board board, int depth);

}
