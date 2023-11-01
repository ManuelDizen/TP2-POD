package ar.edu.itba.pod.MapReduce.mappers;

import ar.edu.itba.pod.MapReduce.models.Ride;
import ar.edu.itba.pod.MapReduce.models.Trip;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Query3Mapper implements Mapper<Long, Trip, Long, Ride>{

    private final Set<Long> stationIds;

    public Query3Mapper(Set<Long> stationIds) {
        this.stationIds = new HashSet<>(stationIds);
    }

    private Integer calculateMinutes(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.toMinutesPart();
    }

    @Override
    public void map(Long key, Trip trip, Context<Long, Ride> context) {
        Long start = trip.getEmplacement_pk_start();
        Long end = trip.getEmplacement_pk_end();
        if(!Objects.equals(start, end)
                && stationIds.contains(start)
                && stationIds.contains(end)) {
            LocalDateTime start_date = trip.getStart_date();
            Integer minutes = calculateMinutes(start_date, trip.getEnd_date());
            Ride ride = new Ride(end, start_date, minutes);
            context.emit(start, ride);
        }
    }
}
