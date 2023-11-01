package ar.edu.itba.pod.MapReduce.mappers;

import ar.edu.itba.pod.MapReduce.models.DayAfflux;
import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Query4Mapper implements Mapper<Long, Trip, Pair<Long, LocalDate>, Integer>{

    private final LocalDate first;
    private final LocalDate last;

    public Query4Mapper(LocalDate first, LocalDate last) {
        this.first = first;
        this.last = last;
    }

    @Override
    public void map(Long aLong, Trip trip, Context<Pair<Long, LocalDate>, Integer> context) {
        Long start = trip.getEmplacement_pk_start();
        Long end = trip.getEmplacement_pk_end();
        LocalDate start_date = LocalDate.from(trip.getStart_date());
        LocalDate end_date = LocalDate.from(trip.getEnd_date());
        // TODO:Modularizar lo de abajo (dos veces lo mismo)
        if(Objects.equals(start, end)){
            if(start_date.getDayOfYear() != end_date.getDayOfYear()
                && isValidStart(start_date, end_date)) {
                //Este if queda feo, xq ademas start y end son el mismo dia, hago una validacion demas
                context.emit(new Pair<>(start, start_date), -1);
                context.emit(new Pair<>(end, end_date), 1);
            }
        }
        else{
            if(isValidFirst(start_date))
                context.emit(new Pair<>(start, start_date), -1);
            if(isValidEnd(end_date))
                context.emit(new Pair<>(end, end_date), 1);
        }
    }

    private boolean isValidStart(LocalDate d1, LocalDate d2){
        return (d1.isAfter(first) || d1.isEqual(first)) && (d2.isBefore(last) && d2.isEqual(last));
        //queda feo pero no encontre un geq/leq
    }

    private boolean isValidFirst(LocalDate d1){
        return (d1.isAfter(first) || d1.isEqual(first));
    }

    private boolean isValidEnd(LocalDate d2){
        return (d2.isBefore(last) && d2.isEqual(last));
    }
}
