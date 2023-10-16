package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public class Move {

    Board board;
    Piece piece;
    int targetCoordinate;

    public Move(Board board, Piece piece, int targetCoordinate) {
        this.board = board;
        this.piece = piece;
        this.targetCoordinate = targetCoordinate;
    }

    public static class MajorMove extends Move{

        public MajorMove(Board board, Piece piece, int targetCoordinate) {
            super(board, piece, targetCoordinate);
        }
    }

    public static class AttackMove extends Move {

        Piece targetPiece;

        public AttackMove(Board board, Piece piece, int targetCoordinate, Piece targetPiece) {
            super(board, piece, targetCoordinate);
            this.targetPiece = targetPiece;
        }
    }
}
