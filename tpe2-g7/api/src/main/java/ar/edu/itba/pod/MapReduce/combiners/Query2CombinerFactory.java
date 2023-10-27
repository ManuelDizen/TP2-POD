package ar.edu.itba.pod.MapReduce.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class Query2CombinerFactory implements CombinerFactory<Integer, Double, Double> {
    @Override
    public Combiner<Double, Double> newCombiner(Integer integer) {
        return new Query2Combiner();
    }
}
