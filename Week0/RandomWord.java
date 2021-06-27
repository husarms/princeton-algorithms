package Week0;/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        double counter = 1;
        String champion = "";
        while (!StdIn.isEmpty()) {
            String word = StdIn.readString();
            boolean isChampion = StdRandom.bernoulli(1 / counter);
            if (isChampion) {
                champion = word;
            }
            counter++;
        }
        StdOut.println(champion);
    }
}
