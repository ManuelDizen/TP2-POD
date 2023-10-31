package ar.edu.itba.pod.MapReduce.combiners;

import ar.edu.itba.pod.MapReduce.models.Afflux;
import ar.edu.itba.pod.MapReduce.models.DayAfflux;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Query4CombinerFactory implements CombinerFactory<Pair<Long, LocalDate>, Long, Long> {

    @Override
    public Combiner<Long, Long> newCombiner(Pair<Long, LocalDate> longLocalDatePair) {
        return new Query4Combiner();
    }

    private static class Query4Combiner extends Combiner<Long, Long>{
        private Map<LocalDate, Long> map = new HashMap<>();
        private Long sum = 0L;

        @Override
        public void reset(){
            map.clear();
        }

        @Override
        public void combine(Long aLong) {
            sum += aLong;
        }

        @Override
        public Long finalizeChunk() {
            return sum;
        }
    }
}
