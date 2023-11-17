package com.chess.integration.engine.player;

import com.chess.integration.engine.board.Board;
import com.chess.integration.engine.board.Move;
import com.chess.integration.engine.board.Tile;
import com.chess.integration.engine.pieces.Alliance;
import com.chess.integration.engine.pieces.Piece;
import com.chess.integration.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player {
    public WhitePlayer(Board board, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    /**
     * *CONDICIONES DE CASTLEO*
     * <p>
     * - El rey y la torre deben de no haberse movido.
     * - Ni la casilla del rey, ni la casilla en la que acabará, ni las casillas entre estas pueden estar en check.
     * - No puede haber ninguna pieza entre el rey y la torre.
     */
    @Override
    public Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves, Collection<Move> opponentLegalMoves) {
        List<Move> kingCastles = new ArrayList<>();

        if (this.king.isFirstMove() && !this.isInCheck()) {
            //Calculate king side castle
            if (!this.board.getTile(61).isOccupied() && !this.board.getTile(62).isOccupied()) {
                Tile rookTile = this.board.getTile(63);
                if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentLegalMoves).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentLegalMoves).isEmpty() &&
                            rookTile.getPiece().getType() == Piece.PieceType.ROOK) {
                        kingCastles.add(new Move.KingsideCastleMove
                                (this.board, this.king, 62, (Rook)rookTile.getPiece(), rookTile.getCoordinate(), 61));
                    }
                }
            }
            //Calculate queen side castle
            if (!this.board.getTile(59).isOccupied()
                    && !this.board.getTile(58).isOccupied()
                    && !this.board.getTile(57).isOccupied()) {
                Tile rookTile = this.board.getTile(56);
                if (rookTile.isOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(59, opponentLegalMoves).isEmpty()
                            && Player.calculateAttacksOnTile(58, opponentLegalMoves).isEmpty()
                            && rookTile.getPiece().getType() == Piece.PieceType.ROOK) {
                        kingCastles.add(new Move.QueensideCastleMove
                                (this.board, this.king, 58, (Rook)rookTile.getPiece(), rookTile.getCoordinate(), 59));
                    }

                }
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
