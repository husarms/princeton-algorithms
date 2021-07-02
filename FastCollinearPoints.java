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

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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

        // Sort in natural order
        Arrays.sort(points);
        ArrayList<Point> temp = new ArrayList<Point>();
        ArrayList<Double> slopes = new ArrayList<Double>();
        ArrayList<Point> startPoints = new ArrayList<Point>();
        ArrayList<Point> endPoints = new ArrayList<Point>();
        Point[] slopePoints = Arrays.copyOf(points, points.length);
        for (int i = 0; i < points.length; i++) {
            Point origin = points[i];
            // Sort by slope in relation to origin
            Arrays.sort(slopePoints);
            Arrays.sort(slopePoints, origin.slopeOrder());
            double lastSlope = Double.NEGATIVE_INFINITY;
            temp.clear();
            temp.add(origin);
            // System.out.println("---------------------");
            // System.out.println("Origin = " + origin);
            for (int j = 0; j < slopePoints.length; j++) {
                double thisSlope = origin.slopeTo(slopePoints[j]);
                boolean theEndMyFriend = j == slopePoints.length - 1;
                // System.out.println(
                //         "Point " + slopePoints[j] + " slope to " + origin + " = " + thisSlope);
                // Slope matches - add to set
                if (thisSlope == lastSlope && thisSlope != Double.NEGATIVE_INFINITY) {
                    temp.add(slopePoints[j]);
                    // System.out.println("Point added to temp, size = " + temp.size());
                }
                if (thisSlope != lastSlope || theEndMyFriend) {
                    if (temp.size() >= 4) {
                        Point startPoint = temp.get(0);
                        Point endPoint = temp.get(temp.size() - 1);
                        // System.out.println(
                        //         "Considering adding segment " + startPoint + " -> " + endPoint);
                        // System.out.println("Temp = " + temp);
                        // System.out.println("Endpoints = " + endPoints);
                        // Check for duplicates
                        boolean isUnique = true;
                        for (int k = 0; k < endPoints.size(); k++) {
                            Point otherStartPoint = startPoints.get(k);
                            Point otherEndPoint = endPoints.get(k);
                            double otherSlope = slopes.get(k);

                            if ((otherStartPoint.equals(startPoint) ||
                                    otherEndPoint.equals(startPoint) ||
                                    otherStartPoint.equals(endPoint) ||
                                    otherEndPoint.equals(endPoint)) &&
                                    Math.abs(otherSlope) == Math.abs(lastSlope)) {
                                // System.out.println(
                                //         ">>> Rejecting segment as not unique "
                                //                 + new LineSegment(
                                //                 startPoint, endPoint));
                                isUnique = false;
                                break;

                            }
                        }
                        if (isUnique) {
                            segments.add(new LineSegment(startPoint, endPoint));
                            // System.out.println(
                            //         "*** Added segment " + new LineSegment(startPoint, endPoint));
                            startPoints.add(startPoint);
                            endPoints.add(endPoint);
                            slopes.add(lastSlope);
                        }
                    }
                    temp.clear();
                    temp.add(origin);
                    temp.add(slopePoints[j]);
                }
                lastSlope = thisSlope;
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
        StdDraw.setXscale(-1000, 32768);
        StdDraw.setYscale(-1000, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints fast = new FastCollinearPoints(points);
        for (LineSegment segment : fast.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
