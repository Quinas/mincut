package experiments;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

class Measure {
    private List<Double> measures;
    private double mean;
    private double std;

    Measure(List<Double> measures) {
        this.measures = measures;
        int n = measures.size();

        double mean = 0;
        for (double x : measures) {
            mean += x;
        }
        mean /= n;

        double std = 0;
        for (double x : measures) {
            std += (x - mean) * (x - mean);
        }
        std = Math.sqrt(std / (n - 1));

        this.mean = mean;
        this.std = std;
    }

    JSONObject toJson() {
        JSONObject measure = new JSONObject();
        JSONArray measures = new JSONArray(this.measures);
        measure.put("measures", measures);
        measure.put("mean", this.mean);
        measure.put("std", this.std);
        return measure;
    }
}
