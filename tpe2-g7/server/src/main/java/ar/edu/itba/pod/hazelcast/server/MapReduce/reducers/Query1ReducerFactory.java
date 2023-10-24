package ar.edu.itba.pod.hazelcast.server.MapReduce.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;
import ar.edu.itba.pod.hazelcast.server.utils.Pair;

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
