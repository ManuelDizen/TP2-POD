package ar.edu.itba.pod.MapReduce.reducers;

import ar.edu.itba.pod.MapReduce.models.Ride;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class Query3ReducerFactory implements ReducerFactory<Long, Ride, Ride> {
    @Override
    public Reducer<Ride, Ride> newReducer(Long id) {
        return new Query3Reducer();
    }

    private static class Query3Reducer extends Reducer<Ride, Ride>{
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
           if (longest.minutes() == ride.minutes()) {
               if (longest.start_date().isAfter(ride.start_date())) {
                   longest = ride;
               }
           }
        }

        @Override
        public Ride finalizeReduce() {
            return longest;
        }
    }
}
