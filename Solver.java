/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private Stack<Board> _solution = new Stack<Board>();
    private int _solutionNumberOfMoves = 0;
    private boolean _isSolvable = false;

    private class Node {
        private Board board;
        private int moves;
        private int manPriority;
        private int manhattan;
        private Node prevNode;

        public Node(Board board, int moves, Node prevNode) {
            this.board = board;
            this.moves = moves;
            this.manhattan = board.manhattan();
            this.manPriority = this.manhattan + moves;
            this.prevNode = prevNode;
        }
    }

    private Comparator<Node> nodeOrder() {
        return new ByManhattanPriority();
    }

    private class ByManhattanPriority implements Comparator<Node> {
        public int compare(Node node1, Node node2) {
            return compareManhattanPriority(node1, node2);
        }
    }

    private int compareManhattanPriority(Node node1, Node node2) {
        return Integer.compare(node1.manPriority, node2.manPriority);
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        Board initialBoard = initial;
        Board twinBoard = initial.twin();

        MinPQ<Node> initialQueue = new MinPQ<Node>(nodeOrder());
        MinPQ<Node> twinQueue = new MinPQ<Node>(nodeOrder());
        Node currentNode = new Node(initialBoard, 0, null);
        Node twinCurrentNode = new Node(twinBoard, 0, null);
        initialQueue.insert(currentNode);
        twinQueue.insert(twinCurrentNode);

        while (true) {
            currentNode = initialQueue.delMin();
            twinCurrentNode = twinQueue.delMin();

            if (currentNode.board.isGoal()) {
                _solutionNumberOfMoves = currentNode.moves;
                _isSolvable = true;
                constructSolution(currentNode);
                break;
            }
            else if (twinCurrentNode.board.isGoal()) {
                _solutionNumberOfMoves = -1;
                _isSolvable = false;
                break;
            }

            // Add neighbors for next node
            Iterable<Board> currentNeighbors = currentNode.board.neighbors();
            Iterable<Board> twinNeighbors = twinCurrentNode.board.neighbors();
            for (Board neighbor : currentNeighbors) {
                // Critical optimization!
                // Ignore any boards that are the same as board from step before
                if (currentNode.prevNode != null && currentNode.prevNode.board.equals(neighbor)) {
                    continue;
                }
                // Ignore boards that are the same as initial board
                if (initialBoard.equals(neighbor)) {
                    continue;
                }
                initialQueue.insert(new Node(neighbor, currentNode.moves + 1, currentNode));
            }

            for (Board neighbor : twinNeighbors) {
                // Critical optimization!
                // Ignore any boards that are the same as board from step before
                if (twinCurrentNode.prevNode != null && twinCurrentNode.prevNode.board
                        .equals(neighbor)) {
                    continue;
                }
                // Ignore boards that are the same as twin board
                if (twinBoard.equals(neighbor)) {
                    continue;
                }
                twinQueue.insert(new Node(neighbor, twinCurrentNode.moves + 1, twinCurrentNode));
            }
        }
    }

    private boolean nodeWithBoardExists(Board board, Node startingNode) {
        boolean nodeExists = false;
        Node rewindNode = startingNode;
        while (rewindNode.prevNode != null) {
            if (rewindNode.board.equals(board)) {
                nodeExists = true;
                break;
            }
            rewindNode = rewindNode.prevNode;
        }
        return nodeExists;
    }

    private void constructSolution(Node goalNode) {
        // construct solution (chase pointers back)
        int numberOfMoves = 0;
        Node currentNode = goalNode;
        _solution.push(currentNode.board);
        while (currentNode.prevNode != null) {
            currentNode = currentNode.prevNode;
            _solution.push(currentNode.board);
            numberOfMoves++;
        }
        _solutionNumberOfMoves = numberOfMoves;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return _isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return _solutionNumberOfMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return _solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            // for (Board board : solver.solution())
            //     StdOut.println(board);
        }
    }

}
