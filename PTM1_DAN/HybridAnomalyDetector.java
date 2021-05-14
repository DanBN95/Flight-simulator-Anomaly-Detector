package test;


import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import Circle;
import Point;
import jdk.internal.vm.compiler.word.Pointer;



//need to import the other two algo beacuse it is posiblie to use it if needed.

public class HybridAnomalyDetector implements TimeSeriesAnomalyDetector {

    List<CorrelatedFeatures> correlatedFeaturesList = new LinkedList<>();

    double dist( Point a, Point b)
    {
        return sqrt(pow(a.X - b.X, 2)
                + pow(a.Y - b.Y, 2));
    }

    Point get_circle_center(double bx, double by,
                            double cx, double cy)
    {
        double B = bx * bx + by * by;
        double C = cx * cx + cy * cy;
        double D = bx * cy - by * cx;
        return new Point((cy * B - by * C) / (2 * D),  (bx * C - cx * B) / (2 * D) );
    }

    Circle circle_from3(Point A,Point B, Point C)
    {
        Point I = get_circle_center(B.X - A.X, B.Y - A.Y,
                C.X - A.X, C.Y - A.Y);

        I.X += A.X;
        I.Y += A.Y;
        return new Circle( I, dist(I, A));
    }

    Circle circle_from(Point A,Point B)
    {
        // Set the center to be the midpoint of A and B
        Point C = { (A.X + B.X) / 2.0, (A.Y + B.Y) / 2.0 };

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
            if (!c.is_inside(p))
                return false;
        return true;
    }

    // Function to return the minimum enclosing
// circle for N <= 3
    Circle min_circle_trivial(Vector<Point> P)
    {
        assert(P.size() <= 3);
        if (P.empty()) {
            return new Circle(new Point( 0, 0 ), 0 );
        }
        else if (P.size() == 1) {
            return  new Circle( P[0], 0 );
        }
        else if (P.size() == 2) {
            return circle_from(P[0], P[1]);
        }

        // To check if MEC can be determined
        // by 2 points only
        for (int i = 0; i < 3; i++) {
            for (int j = i + 1; j < 3; j++) {

                Circle c = circle_from(P[i], P[j]);
                if (is_valid_circle(c, P))
                    return c;
            }
        }
        return circle_from3(P[0], P[1], P[2]);
    }

    // Returns the MEC using Welzl's algorithm
// Takes a set of input points P and a set R
// points on the circle boundary.
// n represents the number of points in P
// that are not yet processed.
    Circle welzl_helper(Vector<Point> P, Vector<Point> R, int n)
    {
        // Base case when all points processed or |R| = 3
        if (n == 0 || R.size() == 3) {
            return min_circle_trivial(R);
        }

        // Pick a random point randomly
        int idx = rand() % n;
        Point p = P[idx];

        // Put the picked point at the end of P
        // since it's more efficient than
        // deleting from the middle of the vector
        swap(P[idx], P[n - 1]);

        // Get the MEC circle d from the
        // set of points P - {p}
        Circle d = welzl_helper(P, R, n - 1);

        // If d contains p, return d
        if (d.is_inside(p)) {
            return d;
        }
        // Otherwise, must be on the boundary of the MEC
        R.push_back(p);
        // Return the MEC for P - {p} and R U {p}
        return welzl_helper(P, R, n - 1);
    }

    Circle welzl(vector<Point> P)
    {
        vector<Point> P_empty= new Vector<Point>();
        vector<Point> P_copy = P;
        random_shuffle(P_copy.begin(), P_copy.end());
        return welzl_helper(P_copy , P_empty, P_copy.size());
    }


    @Override
    public void learnNormal(TimeSeries ts){
        //find the best correlation
        float best_correlated = 0;
        String save_through_feature = "";
        String [] features = ts.FeaturesList();
        for (int i=0;i<ts.getHashMap().size()-1;i++) { //for every feature checking cov with the other features
            String feature_check = features[i];
            float[] v_check = ts.getHashMap().get(feature_check);
            for (int j=i+1;j<ts.getHashMap().size();j++) {
                String through_feature = features[j];
                float[] through_v = ts.getHashMap().get(through_feature);
                if (v_check != through_v && Math.abs(StatLib.pearson(v_check, through_v)) > best_correlated) {
                    best_correlated = Math.abs(StatLib.pearson(v_check, through_v)); //set the best cor
                    save_through_feature = through_feature;
                }
            }
        }


        //if the best correlation is biger/equel the 0.95 that use lineear algo
        if(best_correlated>=0.95){
            Point[] p = new Point[ts.getSizeOfVector()];
            for (int k = 0; k < ts.getSizeOfVector(); k++)
                p[k] = new Point(ts.valueAtIndex(k, feature_check), ts.valueAtIndex(k, save_through_feature));
            Line reg_line = StatLib.linear_reg(p);
            float max_dev = -1;
            for (Point point : p) {
                float result = StatLib.dev(point, reg_line);
                if (result > max_dev)
                    max_dev = result;

            }
            max_dev*=(float)1.1;
            this.correlatedFeaturesList.add(new CorrelatedFeatures(feature_check, save_through_feature, best_correlated, reg_line, max_dev));
            best_correlated = 0;


        }else if(best_correlated<0.5){
            ArrayList<float> curArrayList=new ArrayList<>();
            float curAvg=0,curStiya=0,curZscore=0,maxZscore=0;
            Float[] curArray=null;
            for (int j=0;j<v_check;j++) {
                curArrayList.add(v_check[j]);
                curArray = curArrayList.toArray(new Float[0]);
                curAvg=StatLib.avg(curArray);
                curStiya=Math.sqrt(StatLib.var(curArray));
                curZscore=Math.abs((j+1)-curAvg)/curStiya;
                if(curZscore>maxZscore){
                    maxZscore=curZscore;
                }

            }
            this.correlatedFeaturesList.add(new CorrelatedFeatures(feature_check, best_correlated,maxZscore));
            best_correlated = 0;
            // if the best cor is under 0.5 then use use z sccore


        }else{
            Vector<Point> point_for_cercle=new vector<Point>();
            //otherwise circle
            for(int t=0;t<v_check.length;t++){
                point_for_cercle.add(new Point(v_check[t],through_v[t]));
            }
            Circle data = welzl(point_for_cercle);

            this.correlatedFeaturesList.add(new CorrelatedFeatures(feature_check, save_through_feature, best_correlated, data.getPoint(), data.getradius()));
            best_correlated = 0;
        }



    }
}

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {

        List<AnomalyReport> anomalyReportList = new LinkedList<>();
        String[] features = ts.FeaturesList();
        for (CorrelatedFeatures correlatedFeatures : this.correlatedFeaturesList) {

            if(correlatedFeatures.corrlation>=0.95){
                for (int i = 0; i < ts.getSizeOfVector(); i++) {
                    Point p = new Point(ts.valueAtIndex(i, correlatedFeatures.feature1), ts.valueAtIndex(i, correlatedFeatures.feature2));
                    if (StatLib.dev(p, correlatedFeatures.lin_reg) > correlatedFeatures.threshold)
                        anomalyReportList.add(new AnomalyReport( correlatedFeatures.feature1 + "-"
                                + correlatedFeatures.feature2, (long)i+1));

                }
            }else if(correlatedFeatures.corrlation<0.5){
                int hashSize=ts.getHashMap().size();
                for (int i=0;i<hashSize-1;i++) {
                    float[] curColToCheck = ts.getHashMap().get(features[i]);
                    if(tx.valueAtIndex(features[i])<checkZScore(curColToCheck)){
                        anomalyReportList.add(new AnomalyReport("division in col"+features[i],(long)i+1));
                    }


                }else{

                    Circle check_in_circle = new Circle (correlatedFeatures.point,correlatedFeatures.radius)

                    for (int i = 0; i < ts.getSizeOfVector(); i++) {
                        Point p = new Point(ts.valueAtIndex(i, correlatedFeatures.feature1), ts.valueAtIndex(i, correlatedFeatures.feature2));
                        if (!check_in_circle.is_inside(p))
                            anomalyReportList.add(new AnomalyReport( correlatedFeatures.feature1 + "-"
                                    + correlatedFeatures.feature2, (long)i+1));

                    }
                }
            }

            if (anomalyReportList.isEmpty())
                return null;
            else
                return anomalyReportList;
        }



    }
}



