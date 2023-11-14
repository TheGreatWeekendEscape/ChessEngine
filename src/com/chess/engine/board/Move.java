package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Rook;

public abstract class Move {

    Board board;
    Piece piece;
    int targetCoordinate;
    public static final Move NULL_MOVE = new NullMove();

    private Move(Board board, Piece piece, int targetCoordinate) {
        this.board = board;
        this.piece = piece;
        this.targetCoordinate = targetCoordinate;
    }

    public int getCurrentCoordinate() {
        return this.piece.getPosition();
    }

    public int getTargetCoordinate() {
        return this.targetCoordinate;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board execute() {
        Builder builder = new Builder();
        for (Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            if (!this.piece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.getPiece().movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + this.targetCoordinate;
        result = prime * result + this.piece.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Move)) {
            return false;
        }
        Move otherMove = (Move) o;
        return this.getTargetCoordinate() == otherMove.getTargetCoordinate()
                && this.getPiece() == otherMove.getPiece();
    }

    public static class MajorMove extends Move {
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

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.targetPiece;
        }

        @Override
        public int hashCode() {
            return this.targetPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AttackMove)) {
                return false;
            }
            AttackMove otherAttackMove = (AttackMove) o;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
    }

    public static class PawnMove extends Move {
        public PawnMove(Board board, Piece piece, int targetCoordinate) {
            super(board, piece, targetCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(Board board, Piece piece, int targetCoordinate, Piece targetPiece) {
            super(board, piece, targetCoordinate, targetPiece);
        }
    }

    public static class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(Board board, Piece piece, int targetCoordinate, Piece targetPiece) {
            super(board, piece, targetCoordinate, targetPiece);
        }
    }

    public static class PawnJump extends Move {
        public PawnJump(Board board, Piece piece, int targetCoordinate) {
            super(board, piece, targetCoordinate);
        }

        @Override
        public Board execute() {
            Builder builder = new Builder();
            for (Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.piece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            Pawn movedPawn = (Pawn) this.piece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static class CastleMove extends Move {
        protected Rook castleRook;
        protected int castleRookStartCoordinate;
        protected int castleRookDestinationCoordinate;

        public CastleMove(Board board, Piece piece, int targetCoordinate,
                          Rook castleRook, int castleRookStartCoordinate, int castleRookDestinationCoordinate) {
            super(board, piece, targetCoordinate);
            this.castleRook = castleRook;
            this.castleRookStartCoordinate = castleRookStartCoordinate;
            this.castleRookDestinationCoordinate = castleRookDestinationCoordinate;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        public boolean isCastlingMove() {
            return true;
        }

        public Board execute() {
            Builder builder = new Builder();
            for (Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.piece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.piece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestinationCoordinate, this.castleRook.getAlliance()));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    public static class KingsideCastleMove extends CastleMove {
        public KingsideCastleMove(Board board, Piece piece, int targetCoordinate,
                                  Rook castleRook, int castleRookStartCoordinate, int castleRookDestinationCoordinate) {
            super(board, piece, targetCoordinate, castleRook, castleRookStartCoordinate, castleRookDestinationCoordinate);
        }
    }

    public static class QueensideCastleMove extends CastleMove {
        public QueensideCastleMove(Board board, Piece piece, int targetCoordinate,
                                   Rook castleRook, int castleRookStartCoordinate, int castleRookDestinationCoordinate) {
            super(board, piece, targetCoordinate, castleRook, castleRookStartCoordinate, castleRookDestinationCoordinate);
        }
    }

    public static class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute a null move.");
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("MoveFactory class is not instantiable.");
        }

        ;

        public static Move createMove(Board board, int currentCoordinate, int targetCoordinate) {
            for (Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getTargetCoordinate() == targetCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }
}
