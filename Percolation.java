/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private int numberOfOpenSites = 0;
    private final int nSize;
    private final WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        nSize = n + 1;
        int nSizeSquared = nSize * nSize;
        uf = new WeightedQuickUnionUF(nSizeSquared + 2);
        for (int i = 1; i < nSize; i++) {
            uf.union(nSizeSquared, mapIndex(1, i));
            uf.union(nSizeSquared + 1, mapIndex(nSize - 1, i));
        }
        grid = new boolean[nSize][nSize];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= nSize || col >= nSize) {
            throw new IllegalArgumentException();
        }

        // Already open
        if (isOpen(row, col)) {
            return;
        }

        // As above
        int aboveRow = row - 1;
        if (aboveRow >= 1) {
            if (isOpen(aboveRow, col)) {
                uf.union(mapIndex(aboveRow, col), mapIndex(row, col));
            }
        }

        // So below
        int belowRow = row + 1;
        if (belowRow < nSize) {
            if (isOpen(belowRow, col)) {
                uf.union(mapIndex(row, col), mapIndex(belowRow, col));
            }
        }

        // To the left
        int leftCol = col - 1;
        if (leftCol >= 1) {
            if (isOpen(row, leftCol)) {
                uf.union(mapIndex(row, leftCol), mapIndex(row, col));
            }
        }

        // To the right
        int rightCol = col + 1;
        if (rightCol < nSize) {
            if (isOpen(row, rightCol)) {
                uf.union(mapIndex(row, col), mapIndex(row, rightCol));
            }
        }

        grid[row][col] = true;
        numberOfOpenSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0 || row >= nSize || col >= nSize) {
            throw new IllegalArgumentException();
        }

        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || col < 0 || row >= nSize || col >= nSize) {
            throw new IllegalArgumentException();
        }

        if (!isOpen(row, col)) {
            return false;
        }

        // Does it connect to the top?
        int root = uf.find(mapIndex(row, col));
        int topRoot = uf.find(mapIndex(1, 1));

        return root == topRoot;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // only one row?
        if (nSize - 1 == 1) {
            return isOpen(1, 1);
        }

        // do the top and bottom rows have the same root?
        int topRoot = uf.find(mapIndex(1, 1));
        int bottomRoot = uf.find(mapIndex(nSize - 1, 1));

        return topRoot == bottomRoot;
    }

    private int mapIndex(int row, int col) {
        return (row * (nSize)) + col;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(2);
        System.out.println("Percolates? " + p.percolates());
    }
}
