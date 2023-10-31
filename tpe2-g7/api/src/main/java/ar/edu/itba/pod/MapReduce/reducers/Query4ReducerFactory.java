package ar.edu.itba.pod.MapReduce.reducers;

import ar.edu.itba.pod.MapReduce.models.Afflux;
import ar.edu.itba.pod.MapReduce.models.DayAfflux;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Query4ReducerFactory implements ReducerFactory<Pair<Long, LocalDate>, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(Long id) {
        return new Query4Reducer();
    }

    private static class Query4Reducer extends Reducer<Long, Long>{
        private Long sum = 0L;

        @Override
        public void reduce(Long aLong) {
            sum += aLong;
        }

        //Quiero quedarme con la cantidad de dias donde la afluencia neta fue positiva
        //neutral o negativa
        @Override
        public Long finalizeReduce() {
            return sum > 0? 1L :
                    (sum < 0 ? -1L : 0L);
        }
    }
}
