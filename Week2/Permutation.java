package Week2;/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue queue = new RandomizedQueue();
        int numberOfIterations = Integer.parseInt(args[0]);

        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            queue.enqueue(word);
        }

        for (int i = 0; i < numberOfIterations; i++) {
            Object item = queue.dequeue();
            StdOut.println(item);
        }
    }
}
