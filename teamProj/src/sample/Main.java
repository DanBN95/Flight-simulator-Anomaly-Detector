package sample;

import View.View;
import com.sun.webkit.Timer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Model;

import java.awt.image.VolatileImage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage)  {
//        FXMLLoader fxl = new FXMLLoader();
//        Parent root = fxl.load(getClass().getResource("sample.fxml").openStream());
//        View view = fxl.getController(); // set the view
//        primaryStage.setTitle("Hello World");
//        view.paint();
//
//        Scene scene = new Scene(root,400,400);
//        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//        primaryStage.setScene(scene);
//        primaryStage.show();

        try {

            FXMLLoader fxl = new FXMLLoader();
            BorderPane root = fxl.load(getClass().getResource("sample.fxml").openStream());
            View view = fxl.getController();
            Model model = new Model("properties.xml");

            view.paint();

            primaryStage.setTitle("Anomaly Detection");
            primaryStage.setScene(new Scene(root, 400, 400));
            primaryStage.show();


            primaryStage.setScene(new Scene(root, 300, 275));
            primaryStage.show();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
