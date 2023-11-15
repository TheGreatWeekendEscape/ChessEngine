package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;
import java.util.Objects;

public abstract class Piece {

    protected PieceType type;
    protected final int position;
    protected final Alliance alliance;
    protected boolean firstMove;
    private final int cachedHashCode;

    public Piece(PieceType type, final int position, final Alliance alliance, final boolean isFirstMove) {
        this.type = type;
        this.position = position;
        this.alliance = alliance;
        this.firstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = type.hashCode();
        result = 31 * result + alliance.hashCode();
        result = 31 * result + position;
        result = 31 * result + (isFirstMove() ? 1 : 0);
        return result;
    }

    public abstract Collection<Move>
    calculateLegalMoves(Board board);

    public abstract Piece movePiece(Move move);

    public Alliance getAlliance() {
        return this.alliance;
    }

    public int getPosition() {
        return this.position;
    }

    public PieceType getType() {
        return this.type;
    }

    public boolean isFirstMove() {
        return this.firstMove;
    }

    public int getPieceValue() {
        return this.type.getPieceValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) {
            return false;
        }
        Piece otherPiece = (Piece) o;
        return position == otherPiece.position &&
                firstMove == otherPiece.firstMove &&
                type == otherPiece.type &&
                alliance == otherPiece.alliance;
    }

    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    public enum PieceType {

        PAWN("P", 1),
        KNIGHT("N", 3),
        BISHOP("B", 3),
        ROOK("R", 5),
        QUEEN("Q", 9),
        KING("K", 0);

        private String name;
        private int pieceValue;

        PieceType(String name, int pieceValue) {
            this.name = name;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public int getPieceValue() {
            return this.pieceValue;
        }
    }
}



