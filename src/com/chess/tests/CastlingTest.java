package com.chess.tests;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.MoveTransition;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CastlingTest {

    @Test
    public void testWhiteKingSideCastle() {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t1.getBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t2.getBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));
        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t3.getBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d6")));
        assertTrue(t4.getMoveStatus().isDone());
        final MoveTransition t5 = t4.getBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t4.getBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("e2")));
        assertTrue(t5.getMoveStatus().isDone());
        final MoveTransition t6 = t5.getBoard()
                .getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(t5.getBoard(), BoardUtils.getCoordinateAtPosition("d6"),
                        BoardUtils.getCoordinateAtPosition("d5")));
        assertTrue(t6.getMoveStatus().isDone());
        final Move wm1 = Move.MoveFactory
                .createMove(t6.getBoard(), BoardUtils.getCoordinateAtPosition(
                        "e1"), BoardUtils.getCoordinateAtPosition("g1"));
        assertTrue(t6.getBoard().getCurrentPlayer().getLegalMoves().contains(wm1));
        final MoveTransition t7 = t6.getBoard().getCurrentPlayer().makeMove(wm1);
        assertTrue(t7.getMoveStatus().isDone());
        assertTrue(t7.getBoard().getWhitePlayer().isCastled());
    }
}
