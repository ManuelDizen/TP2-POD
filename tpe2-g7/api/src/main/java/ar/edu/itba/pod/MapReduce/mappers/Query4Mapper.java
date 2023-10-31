package ar.edu.itba.pod.MapReduce.mappers;

import ar.edu.itba.pod.MapReduce.models.DayAfflux;
import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Query4Mapper implements Mapper<Long, Trip, Pair<Long, LocalDate>, Integer>{

    public Query4Mapper() {}

    @Override
    public void map(Long aLong, Trip trip, Context<Pair<Long, LocalDate>, Integer> context) {
        Long start = trip.getEmplacement_pk_start();
        Long end = trip.getEmplacement_pk_end();
        LocalDate start_date = LocalDate.from(trip.getStart_date());
        LocalDate end_date = LocalDate.from(trip.getEnd_date());
        if(start == end){
            if(start_date.getDayOfYear() != end_date.getDayOfYear()) {
                context.emit(new Pair<>(start, start_date), 0);
                context.emit(new Pair<>(end, end_date), 0);
            }
        }
        else{
            context.emit(new Pair<>(start, start_date), -1);
            context.emit(new Pair<>(end, end_date), 1);
        }
    }
}
