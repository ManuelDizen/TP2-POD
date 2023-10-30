package ar.edu.itba.pod.MapReduce.reducers;

import ar.edu.itba.pod.MapReduce.models.Afflux;
import ar.edu.itba.pod.MapReduce.models.DayAfflux;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Query4ReducerFactory implements ReducerFactory<Long, DayAfflux, Afflux> {
    @Override
    public Reducer<DayAfflux, Afflux> newReducer(Long id) {
        return new Query4Reducer();
    }

    private static class Query4Reducer extends Reducer<DayAfflux, Afflux>{
        private Map<LocalDate, Long> map = new HashMap<>();

        @Override
        public void beginReduce(){
            map.clear();
        }

        //Calculo la afluencia neta de cada dia
        @Override
        public void reduce(DayAfflux dayAfflux) {
            LocalDate date = dayAfflux.date();
            Long afflux = dayAfflux.dayAfflux();
            if(!map.containsKey(date)) {
                map.put(date,afflux);
            }
            else {
                map.put(date, map.get(date)+afflux);
            }
        }

        //Quiero quedarme con la cantidad de dias donde la afluencia neta fue positiva
        //neutral o negativa
        @Override
        public Afflux finalizeReduce() {
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
