package PTM1.AnomalyDetector;

import PTM1.Helpclass.Circle;
import PTM1.Helpclass.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

public class AlgorithmMinCircle {
    public int kl=0;
    private Random rand = new Random();

    public Circle naiveAlgorithm(final List<Point> points) {
        // One point = nul circle
        if (points.size() == 1) {
            return new Circle(new Point(points.get(0).x, points.get(0).y), 0.0);
        }

        Circle minimumCircle = null;

        // General case
        for (final Point p : points) {
            for (final Point q : points) {
                if (p != q) {
                    minimumCircle = new Circle(p, q);
                    if (minimumCircle.containsAllPoints(points)) {
                        return minimumCircle;
                    }
                }
            }
        }

        // Triangle case
        minimumCircle = new Circle((float)0.0,(float) 0.0, Double.MAX_VALUE);
        for (final Point p : points) {
            for (final Point q : points) {
                for (final Point r : points) {
                    System.out.println(this.kl++);
                    if (p != q && q != r && p != r && !Point.areColinear(p, q, r)) {
                        Circle circumscribedCircle = new Circle(p, q, r);
                        if (circumscribedCircle.radius < minimumCircle.radius && circumscribedCircle.containsAllPoints(points)) {
                            minimumCircle = circumscribedCircle;
                        }
                    }
                }
            }
        }

        return minimumCircle;
    }

    public Circle minidisk(final List<Point> points) {
        return bMinidisk(points, new ArrayList<Point>());
    }

    private Circle bMinidisk(final List<Point> points, final List<Point> boundary) {
        Circle minimumCircle = null;

        if (boundary.size() == 3) {
            minimumCircle = new Circle(boundary.get(0), boundary.get(1), boundary.get(2));
        }
        else if (points.isEmpty() && boundary.size() == 2) {
            minimumCircle = new Circle(boundary.get(0), boundary.get(1));
        }
        else if (points.size() == 1 && boundary.isEmpty()) {
            minimumCircle = new Circle(points.get(0).x, points.get(0).y, 0.0);
        }
        else if (points.size() == 1 && boundary.size() == 1) {
            minimumCircle = new Circle(points.get(0), boundary.get(0));
        }
        else {
            Point p = points.remove(rand.nextInt(points.size()));
            minimumCircle = bMinidisk(points, boundary);

            if (minimumCircle != null && !minimumCircle.containsPoint(p)) {
                boundary.add(p);
                minimumCircle = bMinidisk(points, boundary);
                boundary.remove(p);
                points.add(p);
            }
        }

        return minimumCircle;
    }








    double dist(Point a,Point b) {
        return sqrt(pow(a.x - b.x, 2)
                + pow(a.y - b.y, 2));
    }

    Point get_circle_center(double bx, double by, double cx, double cy)
    {
        double B = bx * bx + by * by;
        double C = cx * cx + cy * cy;
        double D = bx * cy - by * cx;
        return new Point((float) ((cy * B - by * C) / (2 * D)), (float)((bx * C - cx * B) / (2 * D)) );
    }
    Circle circle_from3(Point A,Point B,Point C)
    {
        Point I = get_circle_center(B.x- A.x, B.y - A.y,
                C.x - A.x, C.y - A.y);

        I.x += A.x;
        I.y += A.y;
        return new Circle( I, dist(I, A));
    }
    Circle circle_from(Point A,Point B)
    {
        // Set the center to be the midpoint of A and B
        Point C = new Point((float) ((A.x + B.x) / 2.0), (float)((A.y + B.y) / 2.0 ));

        // Set the radius to be half the distance AB
        return new Circle( C, dist(A, B) / 2.0 );
    }
    // Function to check whether a circle
// encloses the given points
    boolean is_valid_circle(Circle c,Vector<Point> P)
    {

        // Iterating through all the points
        // to check  whether the points
        // lie inside the circle or not
        for (Point p : P)
            if (!c.containsPoint(p))
                return false;
        return true;
    }
    // Function to return the minimum enclosing
// circle for N <= 3
    Circle min_circle_trivial(Vector<Point> P)
    {
        assert(P.size() <= 3);
        if (P.size()==0) {
            return new Circle(new Point( 0, 0 ), 0 );
        }
        else if (P.size() == 1) {
            return  new Circle( P.get(0), 0 );
        }
        else if (P.size() == 2) {
            return circle_from(P.get(0), P.get(1));
        }

        // To check if MEC can be determined
        // by 2 points only
        for (int i = 0; i < 3; i++) {
            for (int j = i + 1; j < 3; j++) {

                Circle c = circle_from(P.get(i), P.get(j));
                if (is_valid_circle(c, P))
                    return c;
            }
        }
        return circle_from3(P.get(0), P.get(1), P.get(2));
    }
    Circle welzl_helper(Vector<Point> P, Vector<Point> R, int n)
    {
        // Base case when all points processed or |R| = 3
        if (n == 0 || R.size() == 3) {
            return min_circle_trivial(R);
        }
        Random rand = new Random();
        // Pick a random point randomly
        int idx = rand.nextInt(50) % n;
        Vector<Point> T = P;
        Point out = T.remove(idx);
        // Get the MEC circle d from the
        // set of points P - {p}
        Circle d = welzl_helper(T, R, n - 1);

        // If d contains p, return d
        if (d.containsPoint(out)) {
            return d;
        }
        // Otherwise, must be on the boundary of the MEC
        R.add(out);
        // Return the MEC for P - {p} and R U {p}
        return welzl_helper(T, R, n - 1);
    }
    private void swap(Point point,Point point1) {

        Point temp=point;
        point=point1;
        point1=temp;
    }
    Circle welzl(Vector<Point> P)
    {
        Vector<Point> P_empty= new Vector<Point>();
        Vector<Point> P_copy = P;
        random_shuffle(P_copy.firstElement(), P_copy.get(P_copy.size()-1));
        return welzl_helper(P_copy , P_empty, P_copy.size());
    }
    private void random_shuffle(Point firstElement,Point point) { }

}
