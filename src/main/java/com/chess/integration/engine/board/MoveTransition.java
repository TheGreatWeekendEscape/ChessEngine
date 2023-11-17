package com.chess.integration.engine.board;

public class MoveTransition {

    private final Board board;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(Board board, Move move, MoveStatus moveStatus) {
        this.board = board;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getBoard() { return this.board; };

    public Move getMove() {return this.move; }
}
