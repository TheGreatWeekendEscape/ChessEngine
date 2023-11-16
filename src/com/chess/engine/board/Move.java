package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Rook;

public abstract class Move {

    protected final Board board;
    protected final Piece piece;
    protected final int targetCoordinate;
    protected final boolean isFirstMove;

    private Move(Board board, Piece piece, int targetCoordinate) {
        this.board = board;
        this.piece = piece;
        this.targetCoordinate = targetCoordinate;
        this.isFirstMove = piece.isFirstMove();
    }

    private Move(Board board, int targetCoordinate) {
        this.board = board;
        this.targetCoordinate = targetCoordinate;
        this.piece = null;
        this.isFirstMove = false;
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
    public Board getBoard() { return this.board; }
    public boolean isAttack() {
        return false;
    }
    public boolean isCastleMove() { return false; }
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
        result = prime * result + this.piece.getPosition();
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
                && this.getPiece() == otherMove.getPiece()
                && this.getCurrentCoordinate() == otherMove.getCurrentCoordinate();
    }

    public static class MajorMove extends Move {
        public MajorMove(Board board, Piece piece, int targetCoordinate) {
            super(board, piece, targetCoordinate);
        }

        @Override
        public boolean equals(Object o) {
            return this == o ||o instanceof MajorMove && super.equals(o);
        }

        @Override
        public String toString() {
            return piece.getType().toString() + BoardUtils.getPositionAtCoordinate(this.targetCoordinate);
        }
    }

    public static class AttackMove extends Move {
        Piece targetPiece;

        public AttackMove(Board board, Piece piece, int targetCoordinate, Piece targetPiece) {
            super(board, piece, targetCoordinate);
            this.targetPiece = targetPiece;
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

    public static class MajorAttackMove extends AttackMove {

        public MajorAttackMove(Board board, Piece piece, int targetCoordinate, Piece targetPiece) {
            super(board, piece, targetCoordinate, targetPiece);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof MajorAttackMove && super.equals(o);
        }

        @Override
        public String toString() {
            return piece.getType() + BoardUtils.getPositionAtCoordinate(this.targetCoordinate);
        }
    }

    public static class PawnMove extends Move {
        public PawnMove(Board board, Piece piece, int targetCoordinate) {
            super(board, piece, targetCoordinate);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof PawnMove && super.equals(o);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.targetCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove {
        public PawnAttackMove(Board board, Piece piece, int targetCoordinate, Piece targetPiece) {
            super(board, piece, targetCoordinate, targetPiece);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof PawnAttackMove && super.equals(o);
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.piece.getPosition()).charAt(0) + "x"
                    + BoardUtils.getPositionAtCoordinate(this.targetCoordinate);
        }
    }

    public static class PawnEnPassantAttackMove extends PawnAttackMove {
        public PawnEnPassantAttackMove(Board board, Piece piece, int targetCoordinate, Piece targetPiece) {
            super(board, piece, targetCoordinate, targetPiece);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof PawnEnPassantAttackMove && super.equals(o);
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
                if (!piece.equals(this.getAttackedPiece())) {
                    builder.setPiece(piece);
                }
            }

            builder.setPiece(this.piece.movePiece(this));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
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

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.getTargetCoordinate());
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

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestinationCoordinate;
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CastleMove)) {
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove) o;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }

        @Override
        public boolean isCastleMove() {
            return true;
        }

        @Override
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

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof KingsideCastleMove && super.equals(o);
        }

        @Override
        public String toString() {
            return "0-0";
        }
    }

    public static class QueensideCastleMove extends CastleMove {
        public QueensideCastleMove(Board board, Piece piece, int targetCoordinate,
                                   Rook castleRook, int castleRookStartCoordinate, int castleRookDestinationCoordinate) {
            super(board, piece, targetCoordinate, castleRook, castleRookStartCoordinate, castleRookDestinationCoordinate);
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof QueensideCastleMove && super.equals(o);
        }

        @Override
        public String toString() {
            return "0-0-0";
        }
    }

    public static class NullMove extends Move {
        public NullMove() {
            super(null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute a null move.");
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }
    }

    public static class PawnPromotion extends Move {
        Move decoratedMove;
        Pawn promotedPawn;

        public PawnPromotion(Move decorateMove) {
            super(decorateMove.getBoard(), decorateMove.getPiece(), decorateMove.getTargetCoordinate());
            this.decoratedMove = decorateMove;
            this.promotedPawn = (Pawn) decorateMove.getPiece();
        }

        @Override
        public Board execute() {
            Board pawnMovedBoard = this.decoratedMove.execute();
            Builder builder = new Builder();
            for (Piece piece : pawnMovedBoard.getCurrentPlayer().getActivePieces()) {
                if (!this.promotedPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : pawnMovedBoard.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.getCurrentPlayer().getAlliance());
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(Object o) {
            return this == o || o instanceof PawnPromotion && super.equals(o);
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("MoveFactory class is not instantiable.");
        }

        public static Move createMove(Board board, int currentCoordinate, int targetCoordinate) {
            for (Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getTargetCoordinate() == targetCoordinate) {
                    return move;
                }
            }
            return new NullMove();
        }
    }
}
