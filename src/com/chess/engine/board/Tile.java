package com.chess.engine.board;

import com.chess.engine.pieces.Alliance;
import com.chess.engine.pieces.Piece;

import java.util.HashMap;
import java.util.Map;


import com.google.common.collect.ImmutableMap;

public abstract class Tile {

    final int coordinate;
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private Tile(int coordinate) {
        this.coordinate = coordinate;
    }

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }

        //return Collections.UnmodifableMap(emptyTileMap); Otra forma de hacerlo sin importar Guava.
        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile(final int coordinate, final Piece piece) {
        return piece != null ? new OccupiedTile(coordinate, piece) : EMPTY_TILES_CACHE.get(coordinate);
    }


    public abstract boolean isOccupied();
    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile {

        private EmptyTile(final int coordinate) {
            super(coordinate);
        }

        @Override
        public boolean isOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }
    }

    public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;

        private OccupiedTile(int coordinate, Piece pieceOnTile) {
            super(coordinate);
            this.pieceOnTile = pieceOnTile;
        }

        @Override
        public boolean isOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }

        @Override
        public String toString() {
            return (getPiece().getAlliance() == Alliance.BLACK) ?
                    getPiece().toString().toLowerCase() : getPiece().toString();
        }
    }
}
