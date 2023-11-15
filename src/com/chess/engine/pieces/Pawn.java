package com.chess.engine.pieces;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = {8, 16, 7, 9};

    public Pawn(int position, Alliance alliance) {
        super(PieceType.PAWN, position, alliance, true);
    }

    public Pawn(int position, Alliance alliance, boolean isFirstMove) {
        super(PieceType.PAWN, position, alliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.position + (candidateCoordinateOffset * this.alliance.getDirection());
            if (!BoardUtils.isCoordinateInBoardRange(candidateDestinationCoordinate)) {
                continue;
            }

            if (candidateCoordinateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isOccupied()) {
                //TODO Deal with promotions
                legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
            } else if (candidateCoordinateOffset == 16 && this.firstMove) {
                int behindCandidateDestinationCoordinate = this.position + (this.alliance.getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordinate).isOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isOccupied()) {
                    legalMoves.add(new Move.PawnJump(board, this, candidateDestinationCoordinate));
                }
            } else if (candidateCoordinateOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.position] && this.alliance.equals(Alliance.WHITE) ||
                            (BoardUtils.FIRST_COLUMN[this.position] && this.alliance.equals(Alliance.BLACK))))) {
                if (board.getTile(candidateDestinationCoordinate).isOccupied()) {
                    Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.alliance != pieceOnCandidate.getAlliance()) {
                        //TODO Make new special attack pawn move class
                        legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
            } else if (candidateCoordinateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.position] && this.alliance.equals(Alliance.WHITE) ||
                            (BoardUtils.EIGHTH_COLUMN[this.position] && this.alliance.equals(Alliance.BLACK))))) {
                if (board.getTile(candidateDestinationCoordinate).isOccupied()) {
                    Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.alliance != pieceOnCandidate.getAlliance()) {
                        //TODO Make new special attack pawn move class
                        legalMoves.add(new Move.PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }

            }

        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getTargetCoordinate(), move.getPiece().getAlliance());
    }
}
