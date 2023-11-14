package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {

    Board board;
    Piece piece;
    int targetCoordinate;

    private Move(Board board, Piece piece, int targetCoordinate) {
        this.board = board;
        this.piece = piece;
        this.targetCoordinate = targetCoordinate;
    }

    public int getTargetCoordinate() {
        return this.targetCoordinate;
    }
    public abstract Board execute();

    public static class MajorMove extends Move{

        public MajorMove(Board board, Piece piece, int targetCoordinate) {
            super(board, piece, targetCoordinate);
        }

        @Override
        public Board execute() {
            return null;
        }
    }

    public static class AttackMove extends Move {

        Piece targetPiece;

        public AttackMove(Board board, Piece piece, int targetCoordinate, Piece targetPiece) {
            super(board, piece, targetCoordinate);
            this.targetPiece = targetPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }
}
