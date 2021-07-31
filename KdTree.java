/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;             // root of tree
    private int size = 0;

    private class Node {
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

    private RectHV getRect(Node parentNode, int comparison) {
        double xMin = parentNode.rect.xmin();
        double yMin = parentNode.rect.ymin();
        double xMax = parentNode.rect.xmax();
        double yMax = parentNode.rect.ymax();
        if (comparison < 0) {
            if (parentNode.compareXY.equals("x")) xMax = parentNode.point.x();
            else yMax = parentNode.point.y();
        }
        else {
            if (parentNode.compareXY.equals("x")) xMin = parentNode.point.x();
            else yMin = parentNode.point.y();
        }
        return new RectHV(xMin, yMin, xMax, yMax);
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
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
        traverse(root);
    }

    private void traverse(Node node) {
        StdDraw.setPenRadius(0.0025);
        if (node.compareXY.equals("x")) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(node.point.x(), node.point.y());
        if (node.left != null) {
            traverse(node.left);
        }
        if (node.right != null) {
            traverse(node.right);
        }
    }

    // all points that are inside the rectangle (or on the boundary) (range search)
    public Iterable<Point2D> range(RectHV rect) {
        throw new UnsupportedOperationException();
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (root == null) {
            return null;
        }
        Node node = root;
        Node nearestNode = root;
        double nearestDistance = Double.POSITIVE_INFINITY;
        while (node != null) {
            double distance = node.point.distanceSquaredTo(p);
            if (distance < nearestDistance && !p.equals(node.point)) {
                nearestNode = node;
                nearestDistance = distance;
            }
            int comparison = compare(node.compareXY, node.point, p);
            if (comparison < 0) node = node.left;
            else node = node.right;
        }

        return nearestNode.point;
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

        tree.draw();
    }
}
