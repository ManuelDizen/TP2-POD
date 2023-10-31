package ar.edu.itba.pod.MapReduce.models;

import java.time.LocalDateTime;

public record Ride(Long emplacement_pk_end, LocalDateTime start_date, Integer minutes) {

}
