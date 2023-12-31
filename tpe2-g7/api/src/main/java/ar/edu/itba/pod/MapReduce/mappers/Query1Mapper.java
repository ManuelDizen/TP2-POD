package ar.edu.itba.pod.MapReduce.mappers;

import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Query1Mapper implements Mapper<Long, Trip, Pair<Long, Long>, Long>{
    private static final Long ONE = 1L;
    private final Set<Long> stationIds;

    public Query1Mapper(Set<Long> stationIds) {
        this.stationIds = new HashSet<>(stationIds);
    }

    @Override
    public void map(Long key, Trip trip, Context<Pair<Long,Long>, Long> context) {
        Long start = trip.getEmplacement_pk_start();
        Long end = trip.getEmplacement_pk_end();
        if(!Objects.equals(start, end)
                && stationIds.contains(start)
                && stationIds.contains(end)) {
            Pair<Long, Long> pair = new Pair<>(start, end);
            context.emit(pair, ONE);
        }
    }
}
