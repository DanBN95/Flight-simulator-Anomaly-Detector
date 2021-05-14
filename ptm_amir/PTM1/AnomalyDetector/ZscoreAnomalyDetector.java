package PTM1.AnomalyDetector;

import PTM1.Helpclass.StatLib;
import PTM1.Helpclass.TimeSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ZscoreAnomalyDetector implements TimeSeriesAnomalyDetector {

    private HashMap<String,Float> zscoremap=new HashMap<String,Float>();

    public float MaxcheckZScore(float[] curColToCheck){
        ArrayList<Float> curArrayList=new ArrayList<>();
        float curAvg=0, curStiya=0, curZscore=0, maxZscore=0;
        Float[] curArray;
        curArrayList.add(curColToCheck[0]);
        for (int j=1;j<curColToCheck.length;j++) {
            curArray = curArrayList.toArray(new Float[0]);
            curStiya= (float) Math.sqrt(StatLib.var(curArray));

            curArrayList.add(curColToCheck[j]);
            if(curStiya==0){continue; }
            curAvg=StatLib.avg(curArray);
            curZscore=Math.abs(curColToCheck[j]-curAvg)/curStiya;
            if(curZscore>maxZscore){
                maxZscore=curZscore;
            }
        }
        return maxZscore;
    }



    @Override
    public void learnNormal(TimeSeries ts) {
        int hashSize=ts.getHashMap().size();
        String[] features;
        features = ts.FeaturesList();
        for (int i=0;i<hashSize;i++) {
            float[] curColToCheck = ts.getHashMap().get(features[i]);
            this.zscoremap.put(features[i],MaxcheckZScore(curColToCheck));
        }
    }

    @Override
    public List<AnomalyReport> detect(TimeSeries ts) {
        ts.setvalue("A",30,400);
        List<AnomalyReport> anomalyReportList = new LinkedList<>();
        float curAvg=0, curStiya=0, curZscore=0;
        Float[] curArray;
        int hashSize=ts.getVector_size();
        String[] features = ts.FeaturesList();
        for(int j=0;j<features.length-1;j++) {
            float[] curColToCheck = ts.getHashMap().get(features[j]);
            ArrayList<Float> curArrayList=new ArrayList<>();
            curArrayList.add(curColToCheck[0]);
            for (int i = 1; i < hashSize; i++) {
                //find the the z score and comper to the high z score
                curArray = curArrayList.toArray(new Float[0]);
                curStiya= (float) Math.sqrt(StatLib.var(curArray));
                curArrayList.add(curColToCheck[i]);
                if(curStiya==0){continue;}
                curAvg=StatLib.avg(curArray);
                curZscore=Math.abs(ts.valueAtIndex(i, features[j])-curAvg)/curStiya;
                if ( curZscore >= this.zscoremap.get(features[j])) {
                    anomalyReportList.add(new AnomalyReport("division in col " + features[j], (long) i + 1));
                }
            }
        }
        curAvg=0;
            return anomalyReportList;
    }

}