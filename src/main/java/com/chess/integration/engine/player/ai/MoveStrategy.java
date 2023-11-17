package com.chess.integration.engine.player.ai;

import com.chess.integration.engine.board.Board;
import com.chess.integration.engine.board.Move;

public interface MoveStrategy {

    Move execute(Board board);
}
