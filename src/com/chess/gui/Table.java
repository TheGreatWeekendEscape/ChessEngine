package com.chess.gui;

import com.chess.engine.board.*;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table extends Observable {

    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private final GameSetup gameSetup;
    private Move computerMove;
    private final static String defaultPieceImagesPath = "images/pieceIcons_v2/";
    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private static final Table INSTANCE = new Table();

    public Table() {
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());
        JMenuBar tableMenuBar = createMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setResizable(false);
        this.chessBoard = Board.createStandardBoard();
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.boardPanel = new BoardPanel();
        this.addObserver(new TableGameAIWatcer());
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }

    public void show() {
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }
    public static Table get() {
        return INSTANCE;
    }

    private GameSetup getGameSetup() {
        return this.gameSetup;
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    private JMenuBar createMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem pgnMenuItem = new JMenuItem("Load PGN File");
        pgnMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Opening PGN...");
            }
        });
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileMenu.add(pgnMenuItem);
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private JMenu createPreferencesMenu() {
        JMenu preferencesMenu = new JMenu("Preferences");
        JMenuItem flipBoardMenuItem = new JMenuItem("Flip board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();

        return preferencesMenu;
    }

    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");
        JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
        setupGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });
        optionsMenu.add(setupGameMenuItem);
        return optionsMenu;
    }

    public void setupUpdate(GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    private static class TableGameAIWatcer implements Observer {
        @Override
        public void update(Observable o, Object arg) {

            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrentPlayer()) &&
            !Table.get().getGameBoard().getCurrentPlayer().isInCheckMate() &&
            !Table.get().getGameBoard().getCurrentPlayer().isInStaleMate()) {
                AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }

            if (Table.get().getGameBoard().getCurrentPlayer().isInCheckMate()) {
                System.out.println("Game over " + Table.get().getGameBoard().getCurrentPlayer() + " is in checkmate");
            }
            if (Table.get().getGameBoard().getCurrentPlayer().isInStaleMate()) {
                System.out.println("Game over " + Table.get().getGameBoard().getCurrentPlayer() + " is in stalemate");
            }
        }
    }

    public void updateGameBoard(Board board) {
        this.chessBoard = board;
    }

    public void updateComputerMove(Move move) {
        this.computerMove = move;
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private void moveMadeUpdate(PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }



    private static class AIThinkTank extends SwingWorker<Move, String> {

        private AIThinkTank() {}

        @Override
        protected Move doInBackground() throws Exception {
            MoveStrategy miniMax = new MiniMax(4);
            Move bestMove = miniMax.execute(Table.get().getGameBoard());
            return bestMove;
        }

        @Override
        public void done() {
            try {
                Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().getCurrentPlayer().makeMove(bestMove).getBoard());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);

        abstract BoardDirection opposite();
    }

    private class BoardPanel extends JPanel {
        List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
        }

        public void drawBoard(Board board) {
            removeAll();
            for (TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    enum PlayerType {
        HUMAN, COMPUTER
    }

    private class TilePanel extends JPanel {
        private final int tileCoordinate;
        private final Color lightTileColor = Color.decode("#eeeed2");
        private final Color darkTileColor = Color.decode("#769656");

        TilePanel(BoardPanel boardPanel, int tileCoordinate) {
            super(new GridBagLayout());
            this.tileCoordinate = tileCoordinate;
            setPreferredSize(TILE_PANEL_DIMENSION);
            drawTile(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileCoordinate);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            System.out.println("First move: " + humanMovedPiece.isFirstMove());
                            destinationTile = chessBoard.getTile(tileCoordinate);
                            Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getCoordinate(), destinationTile.getCoordinate());
                            MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                            if (transition.getMoveStatus() == MoveStatus.DONE) {
                                chessBoard = transition.getBoard();
                            }

                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (gameSetup.isAIPlayer(chessBoard.getCurrentPlayer())) {
                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
        }

        public void drawTile(Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegalMoves(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(Board board) {
            this.removeAll();
            if (board.getTile(this.tileCoordinate).isOccupied()) {
                try {
                    BufferedImage image = ImageIO.read(new File
                            (defaultPieceImagesPath + board.getTile(this.tileCoordinate).getPiece().getAlliance().toString().charAt(0)
                                    + board.getTile(this.tileCoordinate).getPiece().toString() + ".png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException ex) {
                    throw new RuntimeException(ex.getMessage());
                }

            }
        }

        private void assignTileColor() {
            boolean isLight = ((tileCoordinate + tileCoordinate / 8) % 2 == 0);
            setBackground(isLight ? lightTileColor : darkTileColor);
        }

        private void highlightLegalMoves(Board board) {
            for (Move move : pieceLegalMoves(board)) {
                if (move.getTargetCoordinate() == this.tileCoordinate) {
                    try {
                        add(new JLabel(new ImageIcon(ImageIO.read(new File("images/highlight.png")))));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex.getMessage());
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getAlliance() == board.getCurrentPlayer().getAlliance()) {
                //return humanMovedPiece.calculateLegalMoves(board);
                return ImmutableList.copyOf(Iterables.concat ( humanMovedPiece.calculateLegalMoves(board),
                        chessBoard.getCurrentPlayer().calculateKingCastles(chessBoard.getCurrentPlayer().getLegalMoves(),
                                chessBoard.getCurrentPlayer().getOpponent().getLegalMoves()) ));
            }
            return Collections.emptyList();
        }

        /*

    For anyone wondering if you configure PieceLegalMoves method in this way,  it will start highlighting castle moves.

    if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
                // return a list of all the legal moves + the castle moves
                return ImmutableList.copyOf(Iterables.concat ( humanMovedPiece.calculateLegalMoves(board),
                      chessBoard.getCurrentPlayer().calculateKingCastles(chessBoard.getCurrentPlayer().getLegalMoves(),
                                                           chessBoard.getCurrentPlayer().getOpponent().getLegalMoves()) ));
            }
            return Collections.emptyList();

         It will start highlighting two moves with the Rook (say, on G1 field, you will have two green dots). Then to fix this minor issue you can go to and add this if condition, which just says if the move is a castling move and the piece is not a king, don't highlight.

        if ( !sourceTile.getPiece().getPieceType().isKing() && move.isCastlingMove()) {
                            continue;
                        }
         */
    }


}
