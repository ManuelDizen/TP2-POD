package ar.edu.itba.pod.hazelcast.server.MapReduce.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;
import ar.edu.itba.pod.hazelcast.server.utils.Pair;

public class Query1CombinerFactory implements CombinerFactory<Pair<Integer>, Long, Long> {

    @Override
    public Combiner<Long, Long> newCombiner(Pair<Integer> integerPair) {
        return new Query1Combiner();
    }
}
