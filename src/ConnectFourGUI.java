import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This is the graphical user interface for the Connect 4 game.
 *
 * The code in this class assumes that methods in ConnectFour.java
 * are working.
 */
public class ConnectFourGUI {
    //sub attributes for ConnectFour class object implementation
    public static final int ROWS = ConnectFour.ROWS;
    public static final int COLUMNS = ConnectFour.COLUMNS;
    public static final char COMPUTER = ConnectFour.COMPUTER;
    public static final char HUMAN = ConnectFour.HUMAN;
    public static final char NONE = ConnectFour.NONE;
    //https://docs.oracle.com/javase/7/docs/api/java/awt/Color.html
    public static final Color COMPUTER_COLOR = Color.BLACK;
    public static final Color HUMAN_COLOR = Color.GREEN;
    public static final Color NONE_COLOR = Color.WHITE;
    public static final Color BOARD_COLOR = Color.BLUE;
    public static final Color UNKNOWN = Color.GREEN;
    /** Array for the game board. */
    private char[][] board;
    /** Which player's turn is it? */
    private char currentPlayer;
    /** How many moves ahead will the computer player search? */
    private int depth;
    // GUI components
    private final JFrame boardFrame;
    private final BoardPanel boardPanel;
    private final JLabel statusLabel;
    //additional GUI components
    //private final JButton newGameButton;

    /**
     * Class used to draw the game board.
     */
    private class BoardPanel extends JPanel {//the extends keywords allows use of the JPanel class

        /**
         * must include long serialVersionUID for default constructor for JPanel
         */
        private static final long serialVersionUID = 1L;

        public BoardPanel() {//creating the GUI board
            setBackground(BOARD_COLOR);
        }

        public int getRowHeight() {//getter for row height
            return getHeight() / ConnectFour.ROWS;
        }

        public int getColumnWidth() {//getter for column width
            return getWidth() / ConnectFour.COLUMNS;
        }

        @Override//method declaration is intended to override a method declaration in a supertype.
        public void paintComponent( Graphics g ) {
            super.paintComponent(g);

            int rowHeight = getRowHeight();
            int colWidth = getColumnWidth();
            int rowOffset = rowHeight / 8;
            int colOffset = colWidth / 8;

            for(int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) {
                    // Flipping columns vertically so row 0 will be at bottom.
                    char piece = board[ROWS-row-1][col];
                    Color color =//ternary operator example
                        piece == HUMAN ? HUMAN_COLOR :
                        piece == COMPUTER ? COMPUTER_COLOR :
                        piece == NONE ? NONE_COLOR :
                        UNKNOWN;
                    g.setColor(color);
                    g.fillOval( col * colWidth + colOffset,
                                row * rowHeight + rowOffset,
                                colWidth - 2*colOffset,
                                rowHeight - 2*rowOffset);
                }
            }
        }
    }//end JPanel subclass

    /**
     * Construct and display GUI for a Connect Four game.
     * @param board The game board
     * @param firstPlayer The char of player who will start.
     * @param depth How far ahead will computer player look?
     */
    public static void showGUI(final char[][] board, 
                               final char firstPlayer,
                               final int depth) {//display the GUI to the user
        // For thread safety, invoke GUI code on event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
                @Override//method declaration is intended to override a method declaration in a supertype.
                public void run() {//entry point to create GUI
                    ConnectFourGUI gui =
                        new ConnectFourGUI(board, firstPlayer, depth);//call class instantiation method
                    gui.startGame();// all threads are complete so run the game
                }
            });
    }

    /** 
     * Handles a mouse click on the board.
     * @param x X position of the click
     * @param y Y position of the click
     */ 
    private void doMouseClick(int x, int y) {
        int column = x / boardPanel.getColumnWidth();

        if( currentPlayer == HUMAN ) {
            if( ConnectFour.isLegalMove(board, column) ) {
                dropPiece(column);
            }
            else {
                System.out.println("Human attempted illegal move at column " + column);
            }
        }
        else {
            System.out.println("Ignoring click on computer's turn");
        }

    }

    /**
     * Drop a piece for the current player in the given column and
     * update for next player's turn.
     * @param column Column where piece should be dropped.
     */
    private void dropPiece(int column) {
        ConnectFour.dropPiece(board,  column, currentPlayer);
        currentPlayer = ConnectFour.getOppositePlayer(currentPlayer);
        boardFrame.repaint();
        //this method causes a call to this component's paint method as soon as possible.
        //Otherwise, this method causes a call to this component's update method as soon
        //as possible.
        checkForWin();
    }

    /**
     * Have the computer make a move.
     */
    private void computerTurn() {
        if(currentPlayer == COMPUTER) {
            // Computer player may take a while so use worker thread
            // to think in the background instead of causing the GUI
            // to lock up.
            SwingWorker<Integer, ?> worker = new SwingWorker<Integer, Object>() {
                @Override//method declaration is intended to override a method declaration in a supertype.
                public Integer doInBackground() {
                    // Make a copy of board for computer to play with
                    // so we won't refresh the GUI with the computer's
                    // thoughts.
                    char[][] boardCopy = new char[ROWS][COLUMNS];
                    for(int i = 0; i < board.length; i++) {//filling copy with status of current board in play
                        boardCopy[i] = java.util.Arrays.copyOf(board[i], board[i].length);
                    }
                    return ConnectFour.bestMoveForComputer(boardCopy, depth);//@see ConnectFour.java
                }

                @Override//method declaration is intended to override a method declaration in a supertype.
                protected void done() {
                    try {
                        int column = get();
                        if( ConnectFour.isLegalMove(board, column) ) {//place board piece
                            dropPiece(column);
                        }
                        else {// Shouldn't happen if ConnectFour methods
                            // are valid, but complain just in case.
                            System.out.println("Computer attempted illegal move at column " + column);
                        }
                    }
                    catch (Exception ex) {//ex variable holds exception message
                        ex.printStackTrace();
                    }
                }
            };
            worker.execute();
        }
        else {
            System.out.println("Ignoring attempted computer play on human's turn.");
        }
    }

    /**
     * See if anyone has won and announce it if they have.
     */
    private void checkForWin() {
        statusLabel.setText("Checking for win...");

        // Checking for win may take some time, so use background
        // thread to keep GUI from locking up.
        SwingWorker<Character, ?> worker = new SwingWorker<Character, Object>() {
            @Override//method declaration is intended to override a method declaration in a supertype.
            public Character doInBackground() {//before any return executes from the current method called
                //execute a check to see if the winner is present
                //using the ConnectFour class method findWinner();
                return ConnectFour.findWinner(board);
            }

            @Override//method declaration is intended to override a method declaration in a supertype.
            protected void done() {
                Character winner = null;
                try {
                    winner = get();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                boolean gameOver = true;
                if( winner.equals(HUMAN) ) {
                    statusLabel.setText("Game over: Human Wins!");
                    JOptionPane.showMessageDialog ( null, "Human Wins!", "Game Over",
                                                    JOptionPane.INFORMATION_MESSAGE );
                }
                else if( winner.equals(COMPUTER) ) {
                    statusLabel.setText("Game over: Computer Wins!");
                    JOptionPane.showMessageDialog ( null, "Computer Wins!", "Game Over",
                                                    JOptionPane.INFORMATION_MESSAGE );
                }
                else if (ConnectFour.isBoardFull(board)) {
                    statusLabel.setText("Game over: Draw");
                    JOptionPane.showMessageDialog ( null, "Draw Game!", "Game Over",
                                                    JOptionPane.INFORMATION_MESSAGE );
                }
                else {
                    gameOver = false;
                    updateStatusLabel();
                }

                if(gameOver) System.exit(0);
                else if(currentPlayer == COMPUTER) computerTurn();
            }
        };
        worker.execute();
    }

    /** Update label under board to say whose turn it is. */
    private void updateStatusLabel() {//executes check for Human after Mouse click
        if(currentPlayer == HUMAN) {
            statusLabel.setText("Human player's turn");
        }
        else if(currentPlayer == COMPUTER) {//executes check for Computer after Mouse click
            statusLabel.setText("Computer player's turn");
        }
        else {// shouldn't happen, but just in case...
            statusLabel.setText("UNKNOWN STATUS");//Error Case
        }
    }
    
    /**
     * Constructor for the GUI. This is the first method called in our program
     */
    private ConnectFourGUI(char[][] board, char player, int depth) {
        //declaring board variables for use in GUI
        this.board = board;
        this.currentPlayer = player;
        this.depth = depth;
        boardFrame = new JFrame();
        boardPanel = new BoardPanel();

        boardFrame.setTitle("Lets' play Connect Four!");

        boardPanel.setPreferredSize( new Dimension(800, 800) );//starting size of the board
        boardPanel.addMouseListener( new MouseAdapter() {//listener for click event
                @Override//method declaration is intended to override a method declaration in a supertype.
                public void mousePressed( MouseEvent e ) {//is this public to only the methods in this ConnectFourGUI class
                    int x = e.getX();
                    int y = e.getY();
                    doMouseClick(x, y);
                }
            });

        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel();
        //Update label under board to say whose turn it is.
        statusPanel.add(statusLabel);
        updateStatusLabel();
        boardFrame.add(boardPanel, BorderLayout.CENTER);
        boardFrame.add(statusPanel, BorderLayout.PAGE_END);

        boardFrame.pack();//Causes this Window to be sized to fit the preferred size and layouts of its subcomponents
        boardFrame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );//Exit the application using the System exit method
        boardFrame.setLocationRelativeTo ( null );//The center point can be obtained with the GraphicsEnvironment.getCenterPoint method.
        boardFrame.setResizable ( true );//allows user cursor to resize view of board
        boardFrame.setVisible ( true ); //displays compiled board to user
    }

    /** Kick off the connect 4 game */
    private void startGame() {
        // Not likely that we were handed a non-empty board, but check anyway
        checkForWin();              
    }

}
