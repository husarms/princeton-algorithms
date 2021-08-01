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

public class KdTree {

    private Node root;             // root of tree
    private int size = 0;
    private Point2D nearestPoint;
    private double nearestDistance;
    private SET<Point2D> range;

    private static class Node {
        private Point2D point;         // associated data
        private RectHV rect;
        private String compareXY;
        private Node left, right;  // left and right subtrees

        public Node(Point2D point, RectHV rect, String compareXY) {
            this.point = point;
            this.rect = rect;
            this.compareXY = compareXY;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    private String flipXY(String xy) {
        if (xy.equals("x")) return "y";
        return "x";
    }

    private int compare(String compareXY, Point2D point1, Point2D point2) {
        if (compareXY.equals("x")) {
            return Double.compare(point2.x(), point1.x());
        }
        return Double.compare(point2.y(), point1.y());
    }

    // Based on comparison get rectangle that point will fall into
    private RectHV getRect(Node parentNode, int comparison) {
        double xMin = parentNode.rect.xmin();
        double yMin = parentNode.rect.ymin();
        double xMax = parentNode.rect.xmax();
        double yMax = parentNode.rect.ymax();
        if (parentNode.compareXY.equals("x")) {
            if (comparison < 0) xMax = parentNode.point.x();
            else xMin = parentNode.point.x();
        }
        else {
            if (comparison < 0) yMax = parentNode.point.y();
            else yMin = parentNode.point.y();
        }
        return new RectHV(xMin, yMin, xMax, yMax);
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) {
            return;
        }
        if (root == null) {
            root = new Node(p, new RectHV(0, 0, 1, 1), "x");
            size++;
            return;
        }
        Node node = root;
        boolean inserted = false;
        while (!inserted) {
            int comparison = compare(node.compareXY, node.point, p);
            if (comparison < 0) { // go left
                if (node.left != null) node = node.left;
                else {
                    RectHV rect = getRect(node, comparison);
                    node.left = new Node(p, rect, flipXY(node.compareXY));
                    size++;
                    inserted = true;
                }
            }
            else { // go right
                if (node.right != null) node = node.right;
                else {
                    RectHV rect = getRect(node, comparison);
                    node.right = new Node(p, rect, flipXY(node.compareXY));
                    size++;
                    inserted = true;
                }
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node node = root;
        while (node != null) {
            if (node.point.equals(p)) return true;
            int comparison = compare(node.compareXY, node.point, p);
            if (comparison < 0) node = node.left;
            else node = node.right;
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        traverseDraw(root);
    }

    private void traverseDraw(Node node) {
        StdDraw.setPenRadius(0.0025);
        if (node.compareXY.equals("x")) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }
        drawPoint(node.point);
        if (node.left != null) {
            traverseDraw(node.left);
        }
        if (node.right != null) {
            traverseDraw(node.right);
        }
    }

    // all points that are inside the rectangle (or on the boundary) (range search)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (root == null) return null;

        range = new SET<Point2D>();
        SET<Point2D> result = traverseRange(root, rect);
        return result;
    }

    private SET<Point2D> traverseRange(Node node, RectHV queryRect) {
        // will not contain this point - no need to continue
        if (!queryRect.intersects(node.rect)) return range;

        // point falls into our query rectangle
        if (queryRect.contains(node.point)) range.add(node.point);

        if (node.left != null) traverseRange(node.left, queryRect);
        if (node.right != null) traverseRange(node.right, queryRect);

        return range;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;

        nearestPoint = new Point2D(2, 2);
        nearestDistance = Double.POSITIVE_INFINITY;
        Point2D result = traverseNearest(root, p);
        return result;
    }

    private Point2D traverseNearest(Node node, Point2D queryPoint) {
        int comparison = compare(node.compareXY, node.point, queryPoint);
        RectHV comparisonRect = getRect(node, comparison);
        double rectangleDistance = comparisonRect.distanceSquaredTo(queryPoint);
        double pointDistance = node.point.distanceSquaredTo(queryPoint);

        if (pointDistance < nearestDistance) {
            nearestDistance = pointDistance;
            nearestPoint = node.point;
        }
        // pruning - if closest point discovered so far is closer than the distance between the query point and
        // the rectangle corresponding to a node - no need to explore that node
        if (nearestDistance < rectangleDistance) {
            return nearestPoint;
        }
        // Two subtrees? Explore based on comparison first
        // if (node.left != null && node.right != null) {
        //
        // }
        if (node.left != null) traverseNearest(node.left, queryPoint);
        if (node.right != null) traverseNearest(node.right, queryPoint);

        return nearestPoint;
    }

    private void drawPoint(Point2D point) {
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(point.x(), point.y());
    }

    private static void drawRectangle(RectHV rectangle) {
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        double midPointX = (rectangle.xmin() + rectangle.xmax()) / 2;
        double midPointY = (rectangle.ymin() + rectangle.ymax()) / 2;
        double halfWidth = (rectangle.xmax() - rectangle.xmin()) / 2;
        double halfHeight = (rectangle.ymax() - rectangle.ymin()) / 2;
        StdDraw.filledRectangle(midPointX, midPointY, halfWidth, halfHeight);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        KdTree tree = new KdTree();

        while (!in.isEmpty()) {
            double i = in.readDouble();
            double j = in.readDouble();
            tree.insert(new Point2D(i, j));
        }

        // tree.draw();
        // Point2D queryPoint = new Point2D(0.1, 0.1);
        // Point2D nearestPoint = tree.nearest(queryPoint);
        // StdDraw.setPenColor(StdDraw.BOOK_RED);
        // StdDraw.setPenRadius(0.04);
        // StdDraw.point(queryPoint.x(), queryPoint.y());
        // StdDraw.setPenColor(StdDraw.BLUE);
        // StdDraw.point(nearestPoint.x(), nearestPoint.y());
        //
        // StdOut.println("Nearest point = " + nearestPoint);

        RectHV queryRect = new RectHV(0.5, 0.4, 0.7, 0.8);
        drawRectangle(queryRect);
        Iterable<Point2D> range = tree.range(queryRect);
        for (Point2D point : range) {
            StdDraw.setPenColor(StdDraw.BOOK_BLUE);
            StdDraw.point(point.x(), point.y());
        }
    }
}
