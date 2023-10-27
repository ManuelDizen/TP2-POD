package ar.edu.itba.pod.MapReduce.combiners;

import com.google.common.util.concurrent.ClosingFuture;
import com.hazelcast.mapreduce.Combiner;

public class Query2Combiner extends Combiner<Double, Double> {
    private double sum;
    private int cant;
    @Override
    public void combine(Double dist) {
        sum += dist;
        cant++;
    }

    @Override
    public Double finalizeChunk() {
        return sum/cant;
    }

    @Override
    public void reset(){
        sum = 0;
        cant = 0;
    }
}
