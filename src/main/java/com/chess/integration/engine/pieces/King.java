package com.chess.integration.engine.pieces;

import com.chess.integration.engine.board.Board;
import com.chess.integration.engine.board.BoardUtils;
import com.chess.integration.engine.board.Move;
import com.chess.integration.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece {

    private boolean castled;
    private static final int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(int position, Alliance alliance) {
        super(PieceType.KING, position, alliance, true);
        this.castled = false;
    }

    public King(int position, Alliance alliance, boolean isFirstMove) {
        super(PieceType.KING, position, alliance, isFirstMove);
        this.castled = false;
    }


    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.position + candidateCoordinateOffset;

            if (isFirstColumnExclusion(this.position, candidateCoordinateOffset) ||
                isEighthColumnExclusion(this.position, candidateCoordinateOffset)) {
                continue;
            }

            if (BoardUtils.isCoordinateInBoardRange(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getAlliance();
                    if (this.getAlliance() != pieceAlliance) {
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }

    public boolean isCastled() {
        return this.castled;
    }

    public void setCastled(boolean castled) {
        this.castled = castled;
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    @Override
    public King movePiece(Move move) {
        if (move.isCastleMove()) {
            this.castled = true;
        }
        King king = new King(move.getTargetCoordinate(), move.getPiece().getAlliance(), false);
        king.setCastled(this.castled);
        return king;
    }
}
