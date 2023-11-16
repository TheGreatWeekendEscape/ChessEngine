package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.MoveStatus;
import com.chess.engine.board.MoveTransition;
import com.chess.engine.pieces.Alliance;

public class MiniMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;

    public MiniMax(int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public Move execute(Board board) {

        long startTime = System.currentTimeMillis();

        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.getCurrentPlayer() + "Thinking with depth = " + searchDepth);

        int numMoves = board.getCurrentPlayer().getLegalMoves().size();
        for (Move move : board.getCurrentPlayer().getLegalMoves()) {
            MoveTransition transition = board.getCurrentPlayer().makeMove(move);
            if (transition.getMoveStatus() == MoveStatus.DONE) {
                currentValue = board.getCurrentPlayer().getAlliance() == Alliance.WHITE ?
                        min(transition.getBoard(), searchDepth -1) :
                        max(transition.getBoard(), searchDepth - 1);
                if (board.getCurrentPlayer().getAlliance() == Alliance.WHITE && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.getCurrentPlayer().getAlliance() == Alliance.BLACK && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }

        long executionTime = System.currentTimeMillis() - startTime;

        return bestMove;
    }

    @Override
    public String toString() {
        return "Minimax";
    }

    public int min(Board board, int depth) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }

        int lowestSeenValue = Integer.MAX_VALUE;
        for (Move move : board.getCurrentPlayer().getLegalMoves()) {
            MoveTransition transition = board.getCurrentPlayer().makeMove(move);
            if (transition.getMoveStatus() == MoveStatus.DONE) {
                int currentValue = max(transition.getBoard(), depth -1);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    public int max(Board board, int depth) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }

        int highestSeenValue = Integer.MIN_VALUE;
        for (Move move : board.getCurrentPlayer().getLegalMoves()) {
            MoveTransition transition = board.getCurrentPlayer().makeMove(move);
            if (transition.getMoveStatus() == MoveStatus.DONE) {
                int currentValue = min(transition.getBoard(), depth -1);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }

    public static boolean isEndGameScenario(Board board) {
        return board.getCurrentPlayer().isInCheckMate() ||
                board.getCurrentPlayer().isInStaleMate();
    }

}
