package Week4;/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    private int[][] _tiles;
    private int _n;
    private int blankSquareRow;
    private int blankSquareColumn;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        _n = tiles.length;
        _tiles = copy(tiles);
        // Find location of empty square (0)
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < _n; j++) {
                int value = _tiles[i][j];
                if (value == 0) {
                    blankSquareRow = i;
                    blankSquareColumn = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(_n + "\n");
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < _n; j++) {
                s.append(String.format("%2d ", _tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return _n;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        int expectedValue = 1;
        for (int i = 0; i < _n; i++) {
            int[] row = _tiles[i];
            for (int j = 0; j < _n; j++) {
                int value = row[j];
                if (value != 0 && value != expectedValue) {
                    hamming++;
                }
                expectedValue++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < _n; j++) {
                int value = _tiles[i][j];
                if (value == 0) continue;
                int correctRowForValue = (int) Math.ceil((double) value / _n);
                int rowEndValue = correctRowForValue * _n;
                int rowStartValue = rowEndValue - _n + 1;
                int correctColumnForValue = value - rowStartValue + 1;
                int rowDifference = Math.abs((i + 1) - correctRowForValue);
                int columnDifference = Math.abs((j + 1) - correctColumnForValue);
                manhattan += rowDifference;
                manhattan += columnDifference;
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        // if (this == y) return true;
        if (y == null) return false;
        if (getClass() != y.getClass()) return false;
        Board boardY = (Board) y;
        return Arrays.deepEquals(boardY._tiles, this._tiles);
    }

    private int[][] copy(int[][] arrayToCopy) {
        int[][] newArray = new int[_n][_n];
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < _n; j++) {
                newArray[i][j] = arrayToCopy[i][j];
            }
        }
        return newArray;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();

        // look down
        int rowDown = blankSquareRow - 1;
        if (rowDown >= 0) {
            int[][] neighbor = copy(_tiles);
            int value = neighbor[rowDown][blankSquareColumn];
            neighbor[rowDown][blankSquareColumn] = _tiles[blankSquareRow][blankSquareColumn];
            neighbor[blankSquareRow][blankSquareColumn] = value;
            neighbors.push(new Board(neighbor));
        }

        // look up
        int rowUp = blankSquareRow + 1;
        if (rowUp <= _n - 1) {
            int[][] neighbor = copy(_tiles);
            int value = neighbor[rowUp][blankSquareColumn];
            neighbor[rowUp][blankSquareColumn] = _tiles[blankSquareRow][blankSquareColumn];
            neighbor[blankSquareRow][blankSquareColumn] = value;
            neighbors.push(new Board(neighbor));
        }

        // look left
        int columnLeft = blankSquareColumn - 1;
        if (columnLeft >= 0) {
            int[][] neighbor = copy(_tiles);
            int value = neighbor[blankSquareRow][columnLeft];
            neighbor[blankSquareRow][columnLeft] = _tiles[blankSquareRow][blankSquareColumn];
            neighbor[blankSquareRow][blankSquareColumn] = value;
            neighbors.push(new Board(neighbor));
        }

        // look right
        int columnRight = blankSquareColumn + 1;
        if (columnRight <= _n - 1) {
            int[][] neighbor = copy(_tiles);
            int value = neighbor[blankSquareRow][columnRight];
            neighbor[blankSquareRow][columnRight] = _tiles[blankSquareRow][blankSquareColumn];
            neighbor[blankSquareRow][blankSquareColumn] = value;
            neighbors.push(new Board(neighbor));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] arrayCopy = copy(_tiles);
        int value1Row = -1;
        int value1Column = -1;
        int value2Row = -1;
        int value2Column = -1;
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < _n; j++) {
                int value = arrayCopy[i][j];
                // Find two non-zero tiles and record their positions
                if (value != 0) {
                    if (value1Row == -1) {
                        value1Row = i;
                        value1Column = j;
                    }
                    else {
                        value2Row = i;
                        value2Column = j;
                        break;
                    }
                }
            }
        }
        // Swap tiles
        int value1 = arrayCopy[value1Row][value1Column];
        arrayCopy[value1Row][value1Column] = arrayCopy[value2Row][value2Column];
        arrayCopy[value2Row][value2Column] = value1;

        return new Board(arrayCopy);
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

}
