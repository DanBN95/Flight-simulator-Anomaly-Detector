package PTM1_DAN;
import java.awt.*;
import  java.lang.Math;

import static java.lang.Math.pow;
import static java.lang.StrictMath.sqrt;

public class Circle {
    Point C;
    double R;
    boolean is_inside(Point p)
    {
        return dist(this.C, p) <= this.R;
    }
    public Circle( Point x, double y){
        this.C=x;
        this.R=y;
    }
    public Circle(Circle p ){
        this.C=p.getPoint();
        this.R=p.getradius();

    }
    double dist( Point a, Point b)
    {
        return sqrt(pow(a.X - b.X, 2)
                + pow(a.Y - b.Y, 2));
    }
    Point getPoint (){return this.C;}
    double getradius(){return this.R;}
};
