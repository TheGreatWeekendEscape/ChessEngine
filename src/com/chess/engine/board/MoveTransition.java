package com.chess.engine.board;

public class MoveTransition {

    private Board board;
    private Move move;
    private MoveStatus moveStatus;

    public MoveTransition(Board board, Move move, MoveStatus moveStatus) {
        this.board = board;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getBoard() { return this.board; };
}
