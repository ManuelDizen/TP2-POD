package ar.edu.itba.pod.MapReduce.combiners;

import ar.edu.itba.pod.MapReduce.models.Afflux;
import ar.edu.itba.pod.MapReduce.models.DayAfflux;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Query4CombinerFactory implements CombinerFactory<Pair<Long, LocalDate>, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(Pair<Long, LocalDate> longLocalDatePair) {
        return new Query4Combiner();
    }

    private static class Query4Combiner extends Combiner<Integer, Integer>{
        private Integer sum = 0;

        @Override
        public void reset(){
            sum = 0;
        }

        @Override
        public void combine(Integer aLong) {
            sum += aLong;
        }

        @Override
        public Integer finalizeChunk() {
            return sum;
        }
    }
}
