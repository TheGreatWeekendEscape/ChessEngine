package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.List;

public abstract class Piece {

    protected final int position;
    protected final Alliance alliance;

    public Piece (final int position, final Alliance alliance) {
        this.position = position;
        this.alliance = alliance;
    }

    public abstract List<Move> calculateLegalMoves(Board board);
}
