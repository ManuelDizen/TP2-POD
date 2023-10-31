package ar.edu.itba.pod.MapReduce.combiners;

import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class Query2CombinerFactory implements CombinerFactory<Long, Pair<Double, Long>, Pair<Double, Long>> {
    @Override
    public Combiner<Pair<Double, Long>, Pair<Double, Long>> newCombiner(Long id) {
        return new Query2Combiner();
    }

    public static class Query2Combiner extends Combiner<Pair<Double, Long>, Pair<Double, Long>> {

        private double sum;
        private long cant;
        @Override
        public void combine(Pair<Double, Long> doubleLongPair) {
            sum += doubleLongPair.getFirst();
            cant += doubleLongPair.getSecond();
        }

        @Override
        public Pair<Double, Long> finalizeChunk() {
            return new Pair<>(sum, cant);
        }

        @Override
        public void reset(){
            sum = 0;
            cant = 0;
        }
    }
}
