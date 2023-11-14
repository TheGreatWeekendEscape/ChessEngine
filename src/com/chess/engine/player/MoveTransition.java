package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {

    private Board board;
    private Move move;
    private MoveStatus moveStatus;

    public MoveTransition(Board board, Move move, MoveStatus moveStatus) {
        this.board = board;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}
