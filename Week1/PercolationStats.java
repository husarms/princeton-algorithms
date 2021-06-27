package Week1;/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] thresholdResults;
    private final int sampleSize;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than zero");
        }
        sampleSize = trials;
        thresholdResults = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int column = StdRandom.uniform(1, n + 1);
                p.open(row, column);
            }
            double thresholdResult = (double) p.numberOfOpenSites() / (n * n);
            thresholdResults[i] = thresholdResult;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholdResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholdResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = mean();
        double marginOfError = marginOfError();
        return mean - marginOfError;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = mean();
        double marginOfError = marginOfError();
        return mean + marginOfError;
    }

    private double marginOfError() {
        double standardDeviation = stddev();
        double standardError = standardDeviation / Math.sqrt(sampleSize);
        double marginOfError = standardError * 2;
        return marginOfError;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println("mean                    = " +
                                   stats.mean());
        System.out.println("stddev                  = " +
                                   stats.stddev());
        System.out.println("95% confidence interval = " +
                                   "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
