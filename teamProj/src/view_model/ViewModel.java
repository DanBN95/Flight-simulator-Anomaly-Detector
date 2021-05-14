package view_model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import model.Model;
import test.TimeSeries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Observable;
import java.util.Observer;

public class ViewModel implements Observer {

    Model model;
    public FloatProperty aileron,elevator,rudder,throttle,altitude,airSpeed,heading;

    // To Do: attach video slider to timestep : timestep.bind(video_timestep)
    int time_step;

    // To Do: build a timeseries with a csv we get from the user,
    TimeSeries timeSeries;
    test.TimeSeriesAnomalyDetector anomalyDtector;

    public ViewModel(Model m) {
        this.model = m;
        timeSeries = new TimeSeries("test.csv");

        /* EXAMPLE
        for every feature get the valueAtIndex(timestep,feature) and update the addListner
         */
        float val = timeSeries.valueAtIndex(10,"aileron");
        m.addObserver(this);

        aileron = new SimpleFloatProperty();
        elevator = new SimpleFloatProperty();
        rudder = new SimpleFloatProperty();
        throttle = new SimpleFloatProperty();
        altitude = new SimpleFloatProperty();
        airSpeed = new SimpleFloatProperty();
        heading = new SimpleFloatProperty();

        aileron.addListener((o,val,newval)->model.setAileron((float)newval));
        elevator.addListener((o,val,newval)->model.setElevator((float)newval));
        rudder.addListener((o,val,newval)->model.setRudder((float)newval));
        throttle.addListener((o,val,newval)->model.setThrottle((float)newval));
        airSpeed.addListener((o,val,newval)->model.setAirSpeed((float)newval));
        heading.addListener((o,val,newval)->model.setHeading((float)newval));

    }

    public void LoadAlgo() {
        //need to deal with exceptions
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
            this.anomalyDtector= Ts;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void update(Observable o, Object arg) {

    }
}
