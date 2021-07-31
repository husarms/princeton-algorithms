/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {

    private final SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.005);
        for (Point2D point : pointSet) {
            StdDraw.point(point.x(), point.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        SET<Point2D> pointsInRange = new SET<Point2D>();
        for (Point2D point : pointSet) {
            if (rect.contains(point)) {
                pointsInRange.add(point);
            }
        }
        return pointsInRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;
        for (Point2D point : pointSet) {
            double distance = point.distanceSquaredTo(p);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        PointSET p = new PointSET();

        while (!in.isEmpty()) {
            double i = in.readDouble();
            double j = in.readDouble();
            p.insert(new Point2D(i, j));
        }

        p.draw();
    }
}
