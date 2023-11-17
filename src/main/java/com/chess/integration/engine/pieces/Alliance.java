package com.chess.integration.engine.pieces;

import com.chess.integration.engine.board.BoardUtils;
import com.chess.integration.engine.player.BlackPlayer;
import com.chess.integration.engine.player.Player;
import com.chess.integration.engine.player.WhitePlayer;

public enum Alliance {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_ROW[position];
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return blackPlayer;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGHTH_ROW[position];
        }
    };

    public abstract int getDirection();
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
    public abstract boolean isPawnPromotionSquare(int position);
}
