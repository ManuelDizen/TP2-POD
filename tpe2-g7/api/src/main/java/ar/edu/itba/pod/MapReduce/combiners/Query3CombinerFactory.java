package ar.edu.itba.pod.MapReduce.combiners;

import ar.edu.itba.pod.MapReduce.models.Ride;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class Query3CombinerFactory implements CombinerFactory<Integer, Ride, Ride> {

    @Override
    public Combiner<Ride, Ride> newCombiner(Integer integer) {
        return new Query3Combiner();
    }
}
