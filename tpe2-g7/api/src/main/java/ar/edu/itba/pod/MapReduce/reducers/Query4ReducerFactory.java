package ar.edu.itba.pod.MapReduce.reducers;

import ar.edu.itba.pod.MapReduce.models.Afflux;
import ar.edu.itba.pod.MapReduce.models.DayAfflux;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Query4ReducerFactory implements ReducerFactory<Pair<Long, LocalDate>, Integer, Integer> {
    @Override
    public Reducer<Integer, Integer> newReducer(Pair<Long, LocalDate> longLocalDatePair) {
        return new Query4Reducer();
    }

    private static class Query4Reducer extends Reducer<Integer, Integer>{
        private Integer sum;

        @Override
        public void beginReduce(){
            sum = 0;
        }
        @Override
        public void reduce(Integer aLong) {
            sum += aLong;
        }

        //Quiero quedarme con la cantidad de dias donde la afluencia neta fue positiva
        //neutral o negativa
        @Override
        public Integer finalizeReduce() {
            return sum > 0? 1 :
                    (sum < 0 ? -1 : 0);
        }
    }
}
