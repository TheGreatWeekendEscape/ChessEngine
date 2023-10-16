package com.chess.engine.board;

public class BoardUtils {

    public static final boolean[] FIRST_COLUMN = initCols(0);
    public static final boolean[] SECOND_COLUMN = initCols(1);
    public static final boolean[] SEVENTH_COLUMN = initCols(6);
    public static final boolean[] EIGHTH_COLUMN = initCols(7);
    public static final int NUM_TILES = 64;
    public static final int TILES_PER_ROW = 8;
    public static boolean isCoordinateInBoardRange(int coordinate) {
        return coordinate >= 0 && coordinate <= 63;
    }

    private static boolean[] initCols(int colNumber) {

        boolean[] cols = new boolean[NUM_TILES];
        for (int i = colNumber; i < cols.length; i += TILES_PER_ROW) {
            cols[i] = true;
        }

        return cols;
    }
}
