package com.chess.integration.tests;

import com.chess.integration.engine.board.Board;
import com.chess.integration.engine.board.BoardUtils;
import com.chess.integration.engine.board.Move;
import com.chess.integration.engine.board.MoveTransition;
import com.chess.integration.engine.pieces.Alliance;
import com.chess.integration.engine.pieces.Bishop;
import com.chess.integration.engine.pieces.King;
import com.chess.integration.engine.pieces.Pawn;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StalemateTest {
    @Test
    public void testAnandKramnikStaleMate() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(21, Alliance.BLACK));
        builder.setPiece(new King(36, Alliance.BLACK, false));
        // White Layout
        builder.setPiece(new Pawn(29, Alliance.WHITE));
        builder.setPiece(new King(31, Alliance.WHITE, false));
        builder.setPiece(new Pawn(39, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.BLACK);
        final Board board = builder.build();
        assertFalse(board.getCurrentPlayer().isInStaleMate());
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e4"),
                        BoardUtils.getCoordinateAtPosition("f5")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getBoard().getCurrentPlayer().isInStaleMate());
        assertFalse(t1.getBoard().getCurrentPlayer().isInCheck());
        assertFalse(t1.getBoard().getCurrentPlayer().isInCheckMate());
    }

    @Test
    public void testAnonymousStaleMate() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(2, Alliance.BLACK, false));
        // White Layout
        builder.setPiece(new Pawn(10, Alliance.WHITE));
        builder.setPiece(new King(26, Alliance.WHITE, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        assertFalse(board.getCurrentPlayer().isInStaleMate());
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("c5"),
                        BoardUtils.getCoordinateAtPosition("c6")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getBoard().getCurrentPlayer().isInStaleMate());
        assertFalse(t1.getBoard().getCurrentPlayer().isInCheck());
        assertFalse(t1.getBoard().getCurrentPlayer().isInCheckMate());
    }

    @Test
    public void testAnonymousStaleMate2() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(0, Alliance.BLACK, false));
        // White Layout
        builder.setPiece(new Pawn(16, Alliance.WHITE));
        builder.setPiece(new King(17, Alliance.WHITE, false));
        builder.setPiece(new Bishop(19, Alliance.WHITE));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        assertFalse(board.getCurrentPlayer().isInStaleMate());
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("a6"),
                        BoardUtils.getCoordinateAtPosition("a7")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getBoard().getCurrentPlayer().isInStaleMate());
        assertFalse(t1.getBoard().getCurrentPlayer().isInCheck());
        assertFalse(t1.getBoard().getCurrentPlayer().isInCheckMate());
    }
}
