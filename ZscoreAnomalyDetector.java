package test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class ZscoreAnomalyDetector implements TimeSeriesAnomalyDetector {

	private hashMap<String,float> tx;

    public float checkZScore(float[] curColToCheck){
        ArrayList<float> curArrayList=new ArrayList<>();
        float curAvg=0, curStiya=0, curZscore=0, maxZscore=0;
        int vectorSize=ts.getSizeOfVector();
        for (int j=0;j<ts.vector_size-2;j++) {
                curArrayList.add(curColToCheck[j]);
                Float[] curArray = curArrayList.toArray(new Float[0]);
                curAvg=StatLib.avg(curArray);
                curStiya=Math.sqrt(StatLib.var(curArray));
                curZscore=Math.abs((j+1)-curAvg)/curStiya;
                if(curZscore>maxZscore){
                   maxZscore=curZscore;
             }
             curArray=null;
         }
         return maxZscore;
    }

	@Override
	public void learnNormal(TimeSeries ts) {
        this.tx=new hashMap<String,float>();
        int hashSize=ts.getHashMap().size();
		String [] features = ts.FeaturesList();
        for (int i=0;i<hashSize-1;i++) { 
		  float[] curColToCheck = ts.getHashMap().get(features[i]);	
          tx.put(features[i], checkZScore(curColToCheck)); 
      }
    }

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		List<AnomalyReport> anomalyReportList = new LinkedList<>();
        int hashSize=ts.getHashMap().size();
		String [] features = ts.FeaturesList();
        for (int i=0;i<hashSize-1;i++) { 
		  float[] curColToCheck = ts.getHashMap().get(features[i]);	
          if(tx.valueAtIndex(features[i])<checkZScore(curColToCheck)){
              anomalyReportList.add(new AnomalyReport("division in col"+features[i],(long)i));
          } 
      }
      	if (anomalyReportList.isEmpty())
			return null;
		else
			return anomalyReportList;
    }

}