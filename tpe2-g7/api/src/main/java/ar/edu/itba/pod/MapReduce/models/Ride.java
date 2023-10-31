package ar.edu.itba.pod.MapReduce.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Ride implements Serializable {

    private final Long emplacement_pk_end;
    private final LocalDateTime start_date;
    private final Integer minutes;

    public Ride(Long emplacement_pk_end, LocalDateTime start_date, Integer minutes) {
        this.emplacement_pk_end = emplacement_pk_end;
        this.start_date = start_date;
        this.minutes = minutes;
    }

    public Long getEmplacement_pk_end() {
        return emplacement_pk_end;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public Integer getMinutes() {
        return minutes;
    }
}
