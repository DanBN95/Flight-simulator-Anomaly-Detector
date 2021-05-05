package model;

import javafx.beans.InvalidationListener;
import sample.UserSettings;
import test.AnomalyDetectionHandler;
import test.TimeSeries;

import javax.sound.sampled.spi.AudioFileReader;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable {

    TimeSeries timeSeries;
    AnomalyDetectionHandler detectionHandler;
    PrintWriter out2fg;
    UserSettings userSettings;
    Socket fg;


    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    public Model(String settings) {
        //delete user settings from this class.
//        this.userSettings = new UserSettings();
//        setSettings(userSettings);
//        serializeToXML(userSettings,settings);
        this.userSettings = new UserSettings();
         userSettings = desrializeFromXML(settings);


//        try {
//            InetAddress ia = InetAddress.getByName(userSettings.getIp());
//            System.out.println(userSettings.getIp());
//            fg = new Socket(userSettings.getIp(), Integer.parseInt(userSettings.getPort()));
//            out2fg = new PrintWriter(fg.getOutputStream());
//
//
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void setSettings(UserSettings settings) {

        //set Deafault locations
        try {
            userSettings.setIp(InetAddress.getLocalHost().toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        userSettings.setPort("5402");
        userSettings.setAileron("/controls/flight/aileron[0]");
        userSettings.setElevator("/controls/flight/elevator");
        userSettings.setRudder("/controls/flight/elevator");
        userSettings.setThrottle("/controls/engines/engine[0]/throttle");
        userSettings.setAltitude("/position/altitude-ft");
        userSettings.setAirSpeed("/instrumentation/airspeed-indicator/indicated-speed-kt");
        userSettings.setHeading("/orientation/heading-deg");
    }

    public UserSettings desrializeFromXML(String xmlFile) {
        try {
            BufferedInputStream file = new BufferedInputStream(new FileInputStream(xmlFile));
            XMLDecoder decoder = new XMLDecoder(file);
            UserSettings decodedSettings = (UserSettings) decoder.readObject();
            decoder.close();
            file.close();
            return decodedSettings;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void serializeToXML(UserSettings us,String settings) {

        try {
            FileOutputStream fos = new FileOutputStream(settings);
            XMLEncoder encoder = new XMLEncoder(fos);
            encoder.setExceptionListener(new ExceptionListener() {
                @Override
                public void exceptionThrown(Exception e) {
                    System.out.println("Exception occurred!");
                }
            });

            // writing to xml file
            encoder.writeObject(us);
            encoder.close();
            fos.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        public void setAileron(float aileron) {
        String command = userSettings.getAileron();
        out2fg.println(command+" "+aileron);
        out2fg.flush();
    }

    public void setElevator(float elevator) {
        String command = userSettings.getElevator();
        out2fg.println(command+" "+elevator);
        out2fg.flush();
    }

    public void setRudder(float rudder) {
        String command = userSettings.getRudder();
        out2fg.println(command+" "+rudder);
        out2fg.flush();
    }

    public void setThrottle(float throttle) {
        String command = userSettings.getThrottle();
        out2fg.println(command+" "+throttle);
        out2fg.flush();
    }

    public void setAltitude(float altitude) {
        String command = userSettings.getAltitude();
        out2fg.println(command+" "+altitude);
        out2fg.flush();
    }

    public void setAirSpeed(float airSpeed) {
        String command = userSettings.getAirSpeed();
        out2fg.println(command+" "+airSpeed);
        out2fg.flush();
    }

    public void setHeading(float heading) {
        String command = userSettings.getHeading();
        out2fg.println(command+" "+heading);
        out2fg.flush();
    }

    public void finalize() {
        try {
            out2fg.close();
            fg.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
