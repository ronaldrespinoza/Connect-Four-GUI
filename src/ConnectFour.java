/**
 * ConnectFour.java
 * <p>
 *  The purpose of this program is to provide controls for the ConnectFourGUI.
 *  These controls are implemented with the following methods.
 *    <h1>1.</h1><p>boardToString()</p>
 *    <h1>2.</h1><p>getNextEmptyRow()</p>
 *    <h1>3.</h1><p>checkWinner()</p>
 *    <h1>4.</h1><p>dropPiece()</p>
 *    <h1>5.</h1><p>isBoardFull()</p>
 *    <h1>6.</h1><p>isLegalMove()</p>
 *    <h1>7.</h1><p>getOppositePlayer()</p>
 *    <h1>8.</h1><p>findLocalWinner()</p>
 *    <h1>9.</h1><p>findWinner()</p>
 *    <h1>10.</h1><p>bestMoveForComputer()</p>
 *    <h1>11.</h1><p>maxScoreForComputer()</p>
 *    <h1>12.</h1><p>minScoreForHuman()</p>
 *    <h1>13.</h1><p>undoDrop()</p>
 *    <h1>14.</h1><p>main()</p>
 * </p>
 * @author Ronald R Espinoza (UNMID: 101508826) CS152L Group 4
 * @author Mike T Dinh (UNMID: 101885067) CS152L Group 4
 * @author Max Morgan Falk (UNMID: 101284979) CS152L Group 4
 * @version 1.0.4, 1 July 2020
 * @param String[] args
 * @return Void
 * @throws N/A
 * @see
 * @since 22 June 2020
 * @serial
 * @deprecated
 */

import java.util.Arrays;

/**
 * Class to play a game of Connect 4.
 */
public class ConnectFour {

    /** Number of columns on the board. */
    public static final int COLUMNS = 7;//controls ConnectFourGUI columns

    /** Number of rows on the board. */
    public static final int ROWS = 6;//controls ConnectFourGUI class rows

    /** Character for computer player's pieces */
    public static final char COMPUTER = 'X';

    /** Character for human player's pieces */
    public static final char HUMAN = 'O';

    /** Character for blank spaces. */
    public static final char NONE = '.';

    /**
     * Creates a string representation of the connect four board.
     * Row 5 is at the bottom. 
     * Rows end with a newline char.
     *
     * @param gameBoard The game board.
     * @return String representation of the board.
     * @author Ronald R Espinoza
     */
    public static String boardToString(char[][] gameBoard) {
        String theBoard = "";
        /* This is a map for the connect four board
            ".......\n" //{[0,0],[0,1],[0,2],[0,3],[0,4],[0,5],[0,6]}
            ".......\n" //{[1,0],[1,1],[1,2],[1,3],[1,4],[1,5],[1,6]}
            ".......\n" //{[2,0],[2,1],[2,2],[2,3],[2,4],[2,5],[2,6]}
            ".......\n" //{[3,0],[3,1],[3,2],[3,3],[3,4],[3,5],[3,6]}
            ".......\n" //{[4,0],[4,1],[4,2],[4,3],[4,4],[4,5],[4,6]}
            ".......\n" //{[5,0],[5,1],[5,2],[5,3],[5,4],[5,5],[5,6]}
        */
        int rowCount = 0;
        int colCount = 0;
        //iterate from the bottom of the board using a reverse row count
        for(rowCount = (ROWS - 1); rowCount >= 0; rowCount--){
            for(colCount = 0; colCount < COLUMNS; colCount++){
                //The check sequence goes {[5][0], [5][1], ... , [0][5], [0][6]}
                theBoard += gameBoard[rowCount][colCount];
            }//appending each new row with a newline character
            theBoard += '\n';
        }
        return theBoard;
    }

    /**
     * Checks the column selected for Play and gets the next empty row
     *
     * @param board The game board.
     * @param column Column in which to drop the piece.
     * @author Ronald R Espinoza
     */
    public static int getNextEmptyRow(char[][] board, int column){
        int row = 0;
        for(row = 0; row < ROWS; row++){
            if(board[row][column] == NONE){
                return row;
            }
            else{}//keep looping for entire row
        }
        //if we got here the row is full
        //System.out.println("in Error state for getNextEmptyRow");
        return -1;
    }

    /**
     * Checks and returns the players piece if a winner is found locally
     *
     * @param cLimit the limit for the column loop
     * @param board The game board.
     * @param row current row from the findWinner() method
     * @param col The index for the start of the for loop.
     * @param rOffset The row offset.
     * @param cOffset The column offset.
     * @author Ronald R Espinoza
     */
    public static char checkWinner(int cLimit, char[][] board, int row,
                                   int col, int rOffset, int cOffset){
        char player = NONE;//set default return to no winner
        int column = col;//our column count does not always begin at 0
        for(col = column; col < cLimit; col++){//limit is controlled from findWinner()
            player = findLocalWinner(board, row, col, rOffset, cOffset);
            if(player != NONE){  return player; }
            //No winner has been found if the player is NONE
        }
        return player;
    }

    /**
     * Drops a piece for given player in column.  Modifies the board
     * array. Assumes the move is legal.
     *
     * @param board The game board.
     * @param column Column in which to drop the piece.
     * @param player Whose piece to drop.
     * @author Max Morgan Falk
     * @author Ronald R Espinoza
     */
    public static void dropPiece(char[][] board, int column, char player){
        int row = 0;//defaulting to bottom row
        row = getNextEmptyRow(board, column);
        if(row == -1){//A piece can not be placed in this column
            System.out.println("This column is full you cannot play here!");
            return;
        }
        else if(board[row][column] == NONE){
            board[row][column] = player;//drops the piece into position
            return;
        }
        else{//Error state and row is not {-1, 0,1,2,3,4,5}
            System.out.print("Something bad happened");
            System.out.print(" at row"+ row +" and column "+column+"!\n");
            return;
        }
    }

    /**
     * Checks if the board is full.
     * @author Mike T Dinh
     * @param board The game board.
     * @return True if board is full, false if not.
     */
    public static boolean isBoardFull(char[][] board) {
        int i,j = 0;

        for (j = 0; j < COLUMNS; j++){//only checking the top row
            if (board[0][j] == NONE){
                return false;
            }
            else{}//check is in a state where piece on the board isn't empty
        }
        for (i = 0; i < ROWS; i++){//checking from the top row down the board
            for (j = 0; j < COLUMNS; j++){//This is because for each column the 
                                          //topmost row will most likely be empty
                if (board[i][j] == NONE){
                    return false;
                }
                else{}//check is in a state where piece on the board isn't empty
            }
        }
        return true;
    }

    /**
     * Checks if dropping a piece into the given column would be a
     * legal move.
     *
     * @param board The game board.
     * @param column The column to check.
     * @return true if column is neither off the edge of the board nor full.
     * @author Ronald R Espinoza
     */
    public static boolean isLegalMove(char[][] board, int column) {
        int row = 0;

        if((column >= 0) && (column < COLUMNS)){//column value is in range
            //proceed with other constraint checking
            row = getNextEmptyRow(board, column);
        }
        else{//column value was not in range
            /* System.out.println("The column selected does not exist!");
            System.out.println("Please select a new column!"); */
            return false;
        }
        if(row == -1){//the column was full
            return false;
        }
        else{//the column still has open positions for play
            return true;
        }
    }

    /**
     * Given the char of a player, return the char for the opponent.
     * Returns human player char when given computer player char.
     * Returns computer player char when given human player char.
     * @author Mike T Dinh
     * @param player Current player character
     * @return Opponent player character
     */
    public static char getOppositePlayer(char player) {
        if (player == COMPUTER){ return HUMAN; }
        else { return COMPUTER; }
    }

    /**
     * Check for a win starting at a given location and heading in a
     * given direction.
     *
     * Returns the char of the player with four in a row starting at
     * row r, column c and advancing rowOffset, colOffset each step.
     * Returns NONE if no player has four in a row here, or if there
     * aren't four spaces starting here.
     *
     * For example, if rowOffset is 1 and colOffset is 0, we would
     * advance the row index by 1 each step, checking for a vertical
     * win. Similarly, if rowOffset is 0 and colOffset is 1, we would
     * check for a horizonal win.
     * @param board The game board.
     * @param r Row index of where win check starts
     * @param c Column index of where win check starts
     * @param rowOffset How much to move row index at each step
     * @param colOffset How much to move column index at each step
     * @return char of winner from given location in given direction
     *         or NONE if no winner there.
     * @author Max Morgan Falk
     * @author Mike Tan Dinh
     */
    public static char findLocalWinner(char[][] board, int r, int c,
                                       int rowOffset, int colOffset) {
        char player = board[r][c];
        int count = 0;

        for (int i = 0; i < 4; i++){//only check four positions based current position
            int rows= r+i*rowOffset;
            int cols= c+i*colOffset;
            //prevents out of bounds error and will break loop if there's a mismatch
            if((rows < 0 )||//happens at negative row value
                    (rows > ROWS - 1 )||//happens for row over bound
                    (cols < 0 )||//happens at negative column value
                    (cols > COLUMNS - 1 )||//happens at column over bound
                    (board[rows][cols] == NONE)//piece must not be playable
                ){
                player=NONE;
                break;
            }
            //checks if the same player occurs multiple times during the loop
            if (player == board[rows][cols]){
                count++;
            }
        }
        //if the same player occurred four times during the loop
        //then it is a connect four, else no winner yet
        if (count == 4){
            return player;
        }
        return NONE;
    }

    /**
     * Checks entire board for a win (4 in a row).
     * As soon as findLocalWinner() finds an actual player
     * instead of returning none, findWinner() will exit
     * this method returning the piece for the current winner
     *
     * @param board The game board.
     * @return char (HUMAN or COMPUTER) of the winner, or NONE if no
     * winner yet.
     * @author Mike Tan Dinh
     * @author Ronald R Espinoza
     */
    public static char findWinner(char[][] board) {
        int row = 0;
        int col = 0;
        int rOffset = 0;
        int cOffset = 0;
        int cLimit = 0;
        char player = NONE;
        if(isBoardFull(board)){//board is in a draw
            return NONE;
        }
        //walk from bottom left of the board up to the right
        for (row = (ROWS -1); row >= 0; row--) {
            if (row < 4) {//a vertical or diagonal win is impossible at rows 4-6
                //checks for right-left diagonal wins at columns 3-7, in rows 0-3,
                //columns 0-2 diagonal win not possible in this direction
                rOffset = 1;  cOffset = -1;  cLimit = COLUMNS; col = 2;
                player = checkWinner(cLimit, board, row, col, rOffset, cOffset);
                if(player != NONE){ return player; }

                //checks for left-right diagonal wins at columns 0-4, in rows 0-3
                //for columns 5-7 a diagonal win not possible in this direction
                rOffset = 1;  cOffset = 1;  cLimit = 5; col = 0;
                player = checkWinner(cLimit, board, row, col, rOffset, cOffset);
                if(player != NONE){ return player; }

                //checks for Vertical winner for all columns and rows less than 4
                rOffset = 1;  cOffset = 0;  cLimit = COLUMNS; col = 0;
                player = checkWinner(cLimit, board, row, col, rOffset, cOffset);
                if(player != NONE){ return player; }
            }
            //checks every row, starting in columns 0-4 for a horizontal win,
            //horizontal win not possible at column 5-7 walking right
            rOffset = 0;  cOffset = 1; cLimit = 5; col = 0;
            player = checkWinner(cLimit, board, row, col, rOffset, cOffset);
            if(player != NONE){ return player; }
        }
        return NONE;
    }

    /**
     * Returns computer player's best move.
     * @param board The game board.
     * @param maxDepth Maximum search depth.
     * @return Column index for computer player's best move.
     * @author Ronald R Espinoza
     */
    public static int bestMoveForComputer(char[][] board, int maxDepth) {

        int bestCol = 0; //a variable that contains best value for column
        int bestResult = -20;//best result for COMPUTER
        int result = 20;//best result for HUMAN
        int col = 0;
        int depth = 0;//depth is set to 0 to pass the deep search test

        for(col = 0; col < COLUMNS; col++){
            if(isLegalMove(board, col)){
                //attempting a move in each column to find the optimal position
                dropPiece(board, col, COMPUTER);
                result = minScoreForHuman(board, maxDepth, depth);
                undoDrop(board, col);
                if(result >= bestResult){//find the max best result based on the attempted move
                    bestResult = result;
                    bestCol = col;
                    //return bestCol;//forces pc to play each empty position from {[5,0],[5,1], ... [0,5],[0,6]}
                }
                else{}//the human is set to win the game
            }
            else{//make sure board is not full
                if(isBoardFull(board)){//error where no moves are possible
                    return -1;
                }
            }
        }
        return bestCol;//this return optimizes the PC result
    }

    /**
     * Returns the value of board with computer to move:
     *     10 if computer can force a win,
     *     -10 if computer cannot avoid a loss,
     *     0 otherwise.
     *
     * @param board The game board.
     * @param maxDepth Maximum search depth.
     * @param depth Current search depth.
     * @author Max Morgan Falk
     */
    public static int maxScoreForComputer(char[][] board, int maxDepth, int depth) {
        char winner = findWinner(board);
        if (winner == COMPUTER) {
            //These two if statements stay the same, as both this and minScoreForHuman
            // would be called by other functions and having consistant returns
            // simplify the readability.
            return 10;
        }
        else if (winner == HUMAN) {
            return -10;
        }
        else if (depth == maxDepth){
            return 0;
        }
        else if (isBoardFull(board)){
            return 0;
        }
        else {
            int bestResult = -20;
            //since 20 represents the worst outcome for human that is worked up
            // from in the other function,
            // we work up from -20 here for the computer
            for (int c = 0; c < COLUMNS; c++) {
                if (isLegalMove(board, c)) {
                    dropPiece(board, c, COMPUTER);
                    //This is where the two functions reference each other
                    int result = minScoreForHuman(board, maxDepth, depth + 1);
                    undoDrop(board, c);
                    if (result > bestResult) {
                        bestResult = result;
                    }
                }
            }
            return bestResult;
        }
    }

    /**
     * Returns the value of board with human to move:
     *    10 if human cannot avoid a loss,
     *    -10 if human can force a win,
     *     0 otherwise.
     *
     * @param board The game board.
     * @param maxDepth Maximum search depth.
     * @param depth Current search depth.
     */
    public static int minScoreForHuman(char[][] board, int maxDepth, int depth) {
        //apply this logic to the maxScoreForComputer() method
        // The comments in this method are rather verbose to help you
        // understand what is going on. I don't expect you to be so
        // wordy in your own code.

        // First, see if anyone is winning already
        char winner = findWinner(board);
        if (winner == COMPUTER) {
            // computer is winning, so human is stuck
            return 10;
        } else if (winner == HUMAN) {
            // human already won, no chance for computer
            return -10;
        } else if (isBoardFull(board) || (depth == maxDepth)) {
            // We either have a tie (full board) or we've searched as
            // far as we can go. Either way, call it a draw.
            return 0;
        } else {
            // At this point, we know there isn't a winner already and
            // that there must be at least one column still available
            // for play. We'll search all possible moves for the human
            // player and decide which one gives the lowest (best for
            // human) score, assuming that the computer would play
            // perfectly.

            // Start off with a value for best result that is larger
            // than any possible result.
            int bestResult = 20;

            // Loop over all columns to test them in turn.
            for (int c = 0; c < COLUMNS; c++) {
                if (isLegalMove(board, c)) {
                    // This column is a legal move. We'll drop a piece
                    // there so we can see how good it is.
                    dropPiece(board, c, HUMAN);
                    // Call maxScoreForComputer to see what the value would be for the
                    // computer's best play. The maxScoreForComputer method will end
                    // up calling minScoreForHuman in a similar fashion in order to
                    // figure out the best result for the computer's
                    // turn, assuming the human will play perfectly in
                    // response.
                    int result = maxScoreForComputer(board, maxDepth, depth + 1);
                    // Now that we have the result, undo the drop so
                    // the board will be like it was before.
                    undoDrop(board, c);

                    if (result <= bestResult) {// in maxScoreForComputer if (result > bestResult) { go ahead update best result with the result
                        // We've found a new best score. Remember it.
                        bestResult = result;
                    }
                }
            }
            return bestResult;
        }
    }


    /**
     * Removes the top piece from column. Modifies board. Assumes
     * column is not empty.
     * @param board The game board.
     * @param column Column with piece to remove.
     */
    public static void undoDrop(char[][] board, int column) {
        // We'll start at the top and loop down the column until we
        // find a row with a piece in it.
        int row = ROWS - 1;
        while(board[row][column] == NONE && row > 0) {
            row--;
        }

        // Set the top row that had a piece to empty again.
        board[row][column] = NONE;
    }

    /** Creates board array and starts game GUI. */
    public static void main(String[] args) {
        // create array for game board
        char[][] board = new char[ROWS][COLUMNS];
        // fill board with empty spaces
        for(int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                board[row][col] = NONE;
            }
        }

        // show the GUI and start the game
        ConnectFourGUI.showGUI(board, HUMAN, 5);
    }

}
