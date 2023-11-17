package com.chess.integration.engine.pieces;

import com.chess.integration.engine.board.Board;
import com.chess.integration.engine.board.Move;

import java.util.Collection;

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

    public int getValue() { return this.type.getPieceValue(); }

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

        PAWN("P", 100),
        KNIGHT("N", 300),
        BISHOP("B", 300),
        ROOK("R", 500),
        QUEEN("Q", 900),
        KING("K", 1000);

        private final String name;
        private final int pieceValue;

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



