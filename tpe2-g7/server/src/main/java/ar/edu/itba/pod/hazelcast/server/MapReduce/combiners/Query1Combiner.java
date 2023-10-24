package ar.edu.itba.pod.hazelcast.server.MapReduce.combiners;

import com.hazelcast.mapreduce.Combiner;

// EJEMPLO EXTRAÍDO DE CLASE TEÓRICA
public class Query1Combiner extends Combiner<Long, Long> {
    private long sum;
    @Override
    public void combine(Long aLong) {
        sum += aLong;
    }

    @Override
    public Long finalizeChunk() {
        return sum;
    }

    @Override
    public void reset(){
        sum = 0;
    }
}
