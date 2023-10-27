package ar.edu.itba.pod.MapReduce.reducers;

import ar.edu.itba.pod.MapReduce.models.Ride;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query3ReducerFactory implements ReducerFactory<Integer, Ride, Ride> {
    @Override
    public Reducer<Ride, Ride> newReducer(Integer integer) {
        return new Query3Reducer();
    }

    private class Query3Reducer extends Reducer<Ride, Ride>{
        private Ride longest;

        @Override
        public void beginReduce(){
            longest = null;
        }
        @Override
        public void reduce(Ride ride) {
           if (longest == null || longest.minutes() < ride.minutes() ) {
               longest = ride;
           }
        }

        @Override
        public Ride finalizeReduce() {
            return longest;
        }
    }
}
