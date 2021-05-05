import test.AnomalyDetectionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;

public class LoadAlgo {


    public test.TimeSeriesAnomalyDetector LoadAlgo() {
        String input,className;
        System.out.println("enter a class directory");
        try {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        input=in.readLine(); // get user input
        System.out.println("enter the class name");
        className=in.readLine();
        in.close();
// load class directory
        URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] {
                new URL("file://"+input)
        });
            Class<?> c = urlClassLoader.loadClass(className);
            test.TimeSeriesAnomalyDetector Ts = (test.TimeSeriesAnomalyDetector) c.newInstance();
            return Ts;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
