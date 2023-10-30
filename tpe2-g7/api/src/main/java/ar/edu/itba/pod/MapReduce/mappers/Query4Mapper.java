package ar.edu.itba.pod.MapReduce.mappers;

import ar.edu.itba.pod.MapReduce.models.DayAfflux;
import ar.edu.itba.pod.MapReduce.models.Trip;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Query4Mapper implements Mapper<Long, Trip, Long, DayAfflux>{

    //TODO: creo que me faltan quedarme con los stationsIDS

    private LocalDate startDate;
    private LocalDate endDate;
    private static final Long ONE = 1L;
    private static final Long MINUS = -1L;

    public Query4Mapper(LocalDate start, LocalDate end) {
        this.startDate = start;
        this.endDate = end;
    }

    private Integer calculateMinutes(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.toMinutesPart();
    }

    //TODO: revisar que se cumpla: Si para un día en una estación no salieron ni
    // llegaron bicicletas entonces se contabiliza como afluencia neta neutra.
    @Override
    public void map(Long key, Trip trip, Context<Long, DayAfflux> context) {
        Long start = trip.getEmplacement_pk_start();
        Long end = trip.getEmplacement_pk_end();
        LocalDate start_date = LocalDate.from(trip.getStart_date());
        LocalDate end_date = LocalDate.from(trip.getEnd_date());
        if(start_date.isBefore(startDate) && end_date.isAfter(endDate)) {
            return;
        }
        context.emit(start, new DayAfflux(start_date, ONE));
        context.emit(end, new DayAfflux(end_date, MINUS));
    }
}
