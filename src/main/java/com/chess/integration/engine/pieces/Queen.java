package com.chess.integration.engine.pieces;

import com.chess.integration.engine.board.Board;
import com.chess.integration.engine.board.BoardUtils;
import com.chess.integration.engine.board.Move;
import com.chess.integration.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece {

    private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    public Queen (int position, Alliance alliance) {
        super(PieceType.QUEEN, position, alliance, true);
    }

    public Queen(int position, Alliance alliance, boolean isFirstMove) {
        super(PieceType.QUEEN, position, alliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        List<Move> legalMoves = new ArrayList<>();

        for (int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.position;

            while (BoardUtils.isCoordinateInBoardRange(candidateDestinationCoordinate)) {
                if (    isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                        isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if (BoardUtils.isCoordinateInBoardRange(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Piece destinationPiece = candidateDestinationTile.getPiece();
                        final Alliance destinationPieceAlliance = destinationPiece.getAlliance();
                        if (this.alliance != destinationPieceAlliance) {
                            legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, destinationPiece));
                        }
                        break;
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

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getTargetCoordinate(), move.getPiece().getAlliance(), false);
    }
}
