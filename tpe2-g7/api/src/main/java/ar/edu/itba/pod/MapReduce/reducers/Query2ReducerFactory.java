package ar.edu.itba.pod.MapReduce.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query2ReducerFactory implements ReducerFactory<Integer, Double, Double> {
    @Override
    public Reducer<Double, Double> newReducer(Integer integer) {
        return new Query2Reducer();
    }

    private class Query2Reducer extends Reducer<Double, Double>{
        private double sum;
        private int cant;

        @Override
        public void beginReduce(){
            sum = 0;
            cant = 0;
        }
        @Override
        public void reduce(Double dist) {
            sum += dist;
            cant++;
        }

        @Override
        public Double finalizeReduce() {
            return sum/cant;
        }
    }
}
