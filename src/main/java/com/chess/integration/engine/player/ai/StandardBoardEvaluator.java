package com.chess.integration.engine.player.ai;

import com.chess.integration.engine.board.Board;
import com.chess.integration.engine.pieces.Piece;
import com.chess.integration.engine.player.Player;

public class StandardBoardEvaluator implements BoardEvaluator {

    private static final int CHECK_BONUS = 50;
    private static final int CHECKMATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;

    @Override
    public int evaluate(Board board, int depth) {
        return scorePlayer(board, board.getWhitePlayer(), depth) - scorePlayer(board, board.getBlackPlayer(), depth);
    }

    private int scorePlayer(Board board, Player player, int depth) {
        return pieceValue(player) + mobility(player) + check(player) + checkmate(player, depth) + castled(player);
    }

    private static int pieceValue(Player player) {
        int pieceValueScore = 0;
        for (Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getValue();
        }
        return pieceValueScore;
    }

    private static int mobility(Player player) {
        return player.getLegalMoves().size();
    }

    private static int check(Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int checkmate(Player player, int depth) {
        return player.getOpponent().isInCheckMate() ? CHECKMATE_BONUS * depthBonus(depth) : 0;
    }

    private static int castled(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }
}
