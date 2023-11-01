package ar.edu.itba.pod.MapReduce.reducers;

import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query2ReducerFactory implements ReducerFactory<Long, Pair<Double, Long>, Double> {
    @Override
    public Reducer<Pair<Double, Long>, Double> newReducer(Long id) {
        return new Query2Reducer();
    }

    private static class Query2Reducer extends Reducer<Pair<Double, Long>, Double>{

        private double sum;
        private long cant;
        @Override
        public void beginReduce(){
            sum = 0;
            cant = 0;
        }

        @Override
        public void reduce(Pair<Double, Long> doubleLongPair) {
            sum += doubleLongPair.getFirst();
            cant += doubleLongPair.getSecond();
        }

        @Override
        public Double finalizeReduce() {
            return sum/cant;
        }


    }
}
