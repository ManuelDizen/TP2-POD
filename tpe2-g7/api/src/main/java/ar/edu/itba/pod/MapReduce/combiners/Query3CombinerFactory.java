package ar.edu.itba.pod.MapReduce.combiners;

import ar.edu.itba.pod.MapReduce.models.Ride;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class Query3CombinerFactory implements CombinerFactory<Long, Ride, Ride> {

    @Override
    public Combiner<Ride, Ride> newCombiner(Long id) {
        return new Query3Combiner();
    }

    public class Query3Combiner extends Combiner<Ride, Ride> {
        private Ride longest;
        @Override
        public void combine(Ride ride) {
            if (longest == null || longest.minutes() < ride.minutes() ) {
                longest = ride;
            }
        }

        @Override
        public Ride finalizeChunk() {
            return longest;
        }

        @Override
        public void reset(){
            longest = null;
        }
    }
}
