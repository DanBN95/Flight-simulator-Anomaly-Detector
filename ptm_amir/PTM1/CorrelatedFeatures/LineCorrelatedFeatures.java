package PTM1.CorrelatedFeatures;

import PTM1.Helpclass.Line;

public class LineCorrelatedFeatures extends CorrelatedFeatures {
    public final String feature2;
    public final Line lin_reg;
    public final float threshold;

    public LineCorrelatedFeatures(String feature1, String feature2, float corrlation, Line lin_reg,
                                  float threshold) {
		super(feature1,corrlation);
		this.feature2 =feature2;
		this.lin_reg =lin_reg;
		this.threshold =threshold;
    }
}