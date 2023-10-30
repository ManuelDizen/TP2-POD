package ar.edu.itba.pod.MapReduce.combiners;

import ar.edu.itba.pod.MapReduce.models.Afflux;
import ar.edu.itba.pod.MapReduce.models.DayAfflux;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Query4CombinerFactory implements CombinerFactory<Long, DayAfflux, Afflux> {
    @Override
    public Combiner<DayAfflux, Afflux> newCombiner(Long id) {
        return new Query4Combiner();
    }

    private static class Query4Combiner extends Combiner<DayAfflux, Afflux>{
        private Map<LocalDate, Long> map = new HashMap<>();

        @Override
        public void reset(){
            map.clear();
        }

        @Override
        public void combine(DayAfflux dayAfflux) {
            LocalDate date = dayAfflux.date();
            Long afflux = dayAfflux.dayAfflux();
            if(!map.containsKey(date)) {
                map.put(date,afflux);
            }
            else {
                map.put(date, map.get(date)+afflux);
            }
        }

        @Override
        public Afflux finalizeChunk() {
            int positive = 0;
            int negative = 0;
            int neutral = 0;

            for (long count : map.values()) {
                if (count > 0) {
                    positive++;
                } else if (count < 0) {
                    negative++;
                } else {
                    neutral++;
                }
            }
            return new Afflux(positive, negative, neutral);
        }
    }
}
