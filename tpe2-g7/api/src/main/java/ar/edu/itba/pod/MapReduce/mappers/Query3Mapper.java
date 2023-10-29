package ar.edu.itba.pod.MapReduce.mappers;

import ar.edu.itba.pod.MapReduce.models.Ride;
import ar.edu.itba.pod.MapReduce.models.Trip;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Query3Mapper implements Mapper<Long, Trip, Long, Ride>{

    private Integer calculateMinutes(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.toMinutesPart();
    }

    @Override
    public void map(Long key, Trip trip, Context<Long, Ride> context) {
        //Nota: Se emite con los IDs. La idea es que con un collator al final se cambien los IDs
        // por los nombres, para que viaje menos información y sea mas eficiente.

        Long start = trip.getEmplacement_pk_start();
        Long end = trip.getEmplacement_pk_end();
        LocalDateTime start_date = trip.getStart_date();
        Integer minutes = calculateMinutes(start_date, trip.getEnd_date());
        Ride ride = new Ride(end, start_date, minutes);
        context.emit(start, ride);
    }
}
