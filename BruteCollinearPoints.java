/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            Point point1 = points[i];
            if (point1 == null) {
                throw new IllegalArgumentException();
            }
            for (int j = i + 1; j < points.length; j++) {
                Point point2 = points[j];
                if (point1.equals(point2)) {
                    throw new IllegalArgumentException();
                }
            }
        }

        // sort in natural order to avoid duplicates
        Arrays.sort(points);

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        Point point1 = points[i];
                        Point point2 = points[j];
                        Point point3 = points[k];
                        Point point4 = points[m];
                        double slope1 = point1.slopeTo(point2);
                        double slope2 = point1.slopeTo(point3);
                        double slope3 = point1.slopeTo(point4);
                        if (slope1 == slope2 && slope2 == slope3) {
                            segments.add(new LineSegment(point1, point4));
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments.toArray(), segments.size(), LineSegment[].class);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.setPenRadius(0.01);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        for (LineSegment segment : brute.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
