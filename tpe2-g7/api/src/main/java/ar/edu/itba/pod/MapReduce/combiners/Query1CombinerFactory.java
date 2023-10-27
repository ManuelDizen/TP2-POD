package ar.edu.itba.pod.MapReduce.combiners;

import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class Query1CombinerFactory implements CombinerFactory<Pair<Integer>, Long, Long> {

    @Override
    public Combiner<Long, Long> newCombiner(Pair<Integer> integerPair) {
        return new Query1Combiner();
    }

    private static class Query1Combiner extends Combiner<Long, Long> {
        private long sum;
        @Override
        public void combine(Long aLong) {
            sum += aLong;
        }

        @Override
        public Long finalizeChunk() {
            return sum;
        }

        @Override
        public void reset(){
            sum = 0;
        }
    }

}
