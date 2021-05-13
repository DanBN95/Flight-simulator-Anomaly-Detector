package PTM1.AnomalyDetector;

import PTM1.Helpclass.TimeSeries;

import java.util.List;

public interface TimeSeriesAnomalyDetector {
	void learnNormal(TimeSeries ts);
	List<AnomalyReport> detect(TimeSeries ts);
}
