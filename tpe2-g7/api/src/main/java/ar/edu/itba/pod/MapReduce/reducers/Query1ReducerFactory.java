package ar.edu.itba.pod.MapReduce.reducers;

import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

//EJEMPLO EXTRAIDO DE LA TEORICA
public class Query1ReducerFactory implements ReducerFactory<Pair<Integer>, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(Pair<Integer> integerPair) {
        return new Query1Reducer();
    }

    private class Query1Reducer extends Reducer<Long, Long>{
        private long sum;

        @Override
        public void beginReduce(){
            sum = 0;
        }
        @Override
        public void reduce(Long aLong) {
            sum += aLong;
        }

        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}
