package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        // 先考虑north方向且view方向已变，也就是坐标是新的了
        //四个列column形成四个分析，不过是对称的，所以考虑一列，其余类似，且每列顶部的不用动，只用动剩下三个
        //移动思路：从上往下，行2移到行3，行1移到行2，行0移到行2，（最理想状况）
        board.setViewingPerspective(side);
        int s = board.size();
        for (int i = 0; i < s; i += 1) {
            int count = tile_num(board, i);
            changed = changed | move_top_tile(board, i, s-1);
            if (count > 1){
                changed = changed | move_merge(i,s-count , s-1);
            }
        }
        board.setViewingPerspective(Side.NORTH);
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    public static int tile_num(Board b, int i){
        int s = b.size();
        int count = 0;
        for(int j = 0; j < s; j += 1){
            if(b.tile(i, j) != null){
                count += 1;
            }
        }return count;
    }
    public static  boolean move_top_tile(Board b, int i, int max_bound){ //s-1//
        int s = b.size();
        boolean change;
        change = false;
        for(int j = max_bound - 1; j >= 0; j -= 1){
            Tile t = b.tile(i, j);
            if(t != null){
                int dest_row = j;
                for(int k = j + 1; k <= max_bound; k += 1){
                    if(b.tile(i, k) != null){
                        break;
                    }
                    dest_row = k;
                }
                if(dest_row > j){
                    b.move(i, dest_row, t);
                    change = true;
                }
            }
        }return change;
    }

    public boolean move_merge(int i, int min, int max){
        if(min == max){
            return false;
        }
        if(min + 1 == max){
            Tile t= board.tile(i, min);
            if(t.value() == board.tile(i, max).value()){
                board.move(i, max, t);
                score += 2 * t.value();
                return true;
            }return false;
        }else{
            Tile t = board.tile(i, max - 1);
            if(t.value() == board.tile(i, max).value()){
                board.move(i, max, t);
                score += 2 * t.value();
                move_top_tile(board, i, max - 1);
                move_merge(i, min + 1, max - 1);
                return true;
            }else{
                return move_merge(i, min, max -1);
            }
        }
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        int s = b.size();
        for(int i = 0; i < s; i += 1){
            for(int j = 0; j < s; j += 1){
                if(b.tile(i, j) == null){
                    return true;
                }
            }
        }return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int s = b.size();
        for(int i = 0; i < s; i += 1){
            for(int j = 0; j < s; j += 1){
                if(b.tile(i, j) == null){
                    continue;
                }
                if (b.tile(i, j).value() == MAX_PIECE){
                    return true;
                }
            }
        }return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        if (emptySpaceExists(b)){
            return true;
        }int s = b.size();

        for(int i = 0; i < s; i += 1){
            for(int j = 0; j < s; j += 1){
                int x = b.tile(i, j).value();
                if(is_wrong(i-1, j, s) && b.tile(i-1, j) != null && b.tile(i-1, j).value() == x){
                        return true;
                }
                if(is_wrong(i+1, j, s) && b.tile(i+1, j) != null && b.tile(i+1, j).value() == x){
                    return true;
                }
                if(is_wrong(i, j+1, s) && b.tile(i, j+1) != null && b.tile(i, j+1).value() == x){
                    return true;
                }
                if(is_wrong(i, j-1, s) && b.tile(i, j-1) != null && b.tile(i, j-1).value() == x){
                    return true;
                }
            }
        }return false;
    }
    public static boolean is_wrong(int i, int j, int s){
        return i >= 0 && i < s && j >= 0 && j < s;
    }

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
