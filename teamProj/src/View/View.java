package View;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import view_model.ViewModel;

import java.awt.event.MouseEvent;

public class View{

    @FXML
    Canvas joystick;
    @FXML
    Slider rudder,throttle;

    ViewModel vm;
    boolean mousePushed;
    double mx,my;
    FloatProperty aileron,elevator,altitude,airSpeed,heading;

    public View () {
        aileron = new SimpleFloatProperty();
        elevator = new SimpleFloatProperty();
        altitude = new SimpleFloatProperty();
        airSpeed = new SimpleFloatProperty();
        heading = new SimpleFloatProperty();
        mousePushed = false;
    }

    void init(ViewModel vm) {
        this.vm = vm;
        vm.rudder.bind(rudder.valueProperty());
        vm.throttle.bind(throttle.valueProperty());
        vm.aileron.bind(aileron);
        vm.elevator.bind(elevator);
        vm.altitude.bind(altitude);
        vm.airSpeed.bind(airSpeed);
        vm.heading.bind(heading);

    }

    public void paint() { // To do: attach joystick to features: aileron,elevators
        GraphicsContext gc = joystick.getGraphicsContext2D();
        System.out.println(joystick.getWidth());
        mx = joystick.getWidth()/2;
        my = joystick.getHeight()/2;

        gc.strokeOval(mx-50,my-50,100,100); //painting a circle

    }



    public  void mouseDown(MouseEvent me) {
        if(!mousePushed) {
            mousePushed = true;
            System.out.println("mouse is down");
        }
    }

    public  void mouseUp(MouseEvent me) {
        if(!mousePushed) {
            mousePushed = false;
            System.out.println("mouse is down");
        }
    }

}
