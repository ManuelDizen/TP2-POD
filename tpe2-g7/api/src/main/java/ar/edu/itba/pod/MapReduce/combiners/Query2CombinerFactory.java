package ar.edu.itba.pod.MapReduce.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class Query2CombinerFactory implements CombinerFactory<Long, Double, Double> {
    @Override
    public Combiner<Double, Double> newCombiner(Long id) {
        return new Query2Combiner();
    }

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
}
