package com.chess.engine.board;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class BoardUtils {

    public static final boolean[] FIRST_COLUMN = initCols(0);
    public static final boolean[] SECOND_COLUMN = initCols(1);
    public static final boolean[] SEVENTH_COLUMN = initCols(6);
    public static final boolean[] EIGHTH_COLUMN = initCols(7);
    public static final boolean[] FIRST_ROW = initRows(0);
    public static final boolean[] EIGHTH_ROW = initRows(7);
    public static final int NUM_TILES = 64;
    public static final int TILES_PER_ROW = 8;
    public static final String[] ALGEBRAIC_NOTATION = initAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initPositionToCoordinate();

    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate this class.");
    }

    private static String[] initAlgebraicNotation() {
       return new String[] {
               "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
               "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
               "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
               "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
               "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
               "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
               "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
               "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
       };
    }

    private static Map<String, Integer> initPositionToCoordinate() {
        Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }
        return ImmutableMap.copyOf(positionToCoordinate);
    }

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

    private static boolean[] initRows(int rowNumber) {
        boolean[] rows = new boolean[NUM_TILES];
        for (int i = (rowNumber * TILES_PER_ROW); i < ((rowNumber * TILES_PER_ROW) + TILES_PER_ROW); i++) {
            rows[i] = true;
        }

        return rows;
    }

    public static int getCoordinateAtPosition(String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    public static String getPositionAtCoordinate(int coordinate) {
        return ALGEBRAIC_NOTATION[coordinate];
    }
}
