package ar.edu.itba.pod.MapReduce.mappers;

import ar.edu.itba.pod.MapReduce.models.DayAfflux;
import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Query4Mapper implements Mapper<Long, Trip, Pair<Long, LocalDate>, Long>{

    //TODO: creo que me faltan quedarme con los stationsIDS
    private static final Long ONE = 1L;
    private static final Long MINUSONE = -1L;
    private static final Long ZERO = 0L;

    public Query4Mapper() {}

    @Override
    public void map(Long aLong, Trip trip, Context<Pair<Long, LocalDate>, Long> context) {
        Long start = trip.getEmplacement_pk_start();
        Long end = trip.getEmplacement_pk_end();
        LocalDate start_date = LocalDate.from(trip.getStart_date());
        LocalDate end_date = LocalDate.from(trip.getEnd_date());
        if(start == end){
            context.emit(new Pair<>(start, start_date), ZERO);
            context.emit(new Pair<>(end, end_date), ZERO);
        }
        else{
            context.emit(new Pair<>(start, start_date), MINUSONE);
            context.emit(new Pair<>(end, end_date), ONE);
        }
    }
}
