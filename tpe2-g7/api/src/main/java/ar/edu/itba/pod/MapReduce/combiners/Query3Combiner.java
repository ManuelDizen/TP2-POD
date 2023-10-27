package ar.edu.itba.pod.MapReduce.combiners;

import ar.edu.itba.pod.MapReduce.models.Ride;
import com.hazelcast.mapreduce.Combiner;
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
