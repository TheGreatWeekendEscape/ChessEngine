package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected final int position;
    protected final Alliance alliance;
    protected boolean firstMove = false;

    public Piece (final int position, final Alliance alliance) {
        this.position = position;
        this.alliance = alliance;
    }

    public Alliance getAlliance() {
        return this.alliance;
    }

    public abstract Collection<Move> calculateLegalMoves(Board board);


}
