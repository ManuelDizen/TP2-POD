package ar.edu.itba.pod.MapReduce.mappers;

import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class Query1Mapper implements Mapper<Long, Trip, Pair<Integer>, Long>{
    private static final Long ONE = 1L;

    @Override
    public void map(Long key, Trip trip, Context<Pair<Integer>, Long> context) {
        //Nota: Se emite con los IDs. La idea es que con un collator al final se cambien los IDs
        // por los nombres, para que viaje menos información y sea mas eficiente.

        //Ademas, un combiner recoleta las ocurrencias para emitir un único par

        Long start = trip.getEmplacement_pk_start();
        Long end = trip.getEmplacement_pk_end();
        Pair<Integer> pair = new Pair<>(start, end);
        context.emit(pair, ONE);
    }
}
