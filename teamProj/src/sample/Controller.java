package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //change in sceneBuilder LineChart:
    //LineChart id:LineChar
    //CategoryAxis id:x
    //NumberAxis id:y

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private LineChart<> LineChart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        XYChart.Series series = new XYChart.Series();

        series.getData().add(new XYChart.Data("1",23));
        series.getData().add(new XYChart.Data("2",10));
        series.getData().add(new XYChart.Data("3",67));
        series.getData().add(new XYChart.Data("4",29));
        series.getData().add(new XYChart.Data("5",43));
        LineChart.getData().addAll(series);

    }
}
