package com.chess.integration.tests;

import com.chess.integration.engine.board.Board;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class BoardTest {

    @Test
    public void initialBoard() {
        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.getCurrentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().isCastled());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
    }
}
