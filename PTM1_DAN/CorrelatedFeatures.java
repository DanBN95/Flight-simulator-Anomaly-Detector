package PTM1_DAN;


public class CorrelatedFeatures {
	public final String feature1,feature2;
	public final float corrlation;
	public final Line lin_reg;
	public final float threshold;
	public final Point center;
	public final double radius;
	public final float zscore;


	public CorrelatedFeatures(String feature1, String feature2, float corrlation, Line lin_reg, float threshold) {
		this.feature1 = feature1;
		this.feature2 = feature2;
		this.corrlation = corrlation;
		this.lin_reg = lin_reg;
		this.threshold = threshold;
	}
	public CorrelatedFeatures(String feature1, String feature2, float corrlation, Point center, double radius) {
		this.feature1 = feature1;
		this.feature2 = feature2;
		this.corrlation = corrlation;
		this.center= center;
		this.radius= radius;
	}

	public CorrelatedFeatures(String feature1, float corrlation, float zscore) {
		this.feature1 = feature1;
		this.corrlation = corrlation;
		this.zscore=zscore;


	}



}
