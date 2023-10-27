package ar.edu.itba.pod.MapReduce.models;

import com.google.type.DateTime;

import java.time.LocalDateTime;

public class Trip {
    private final LocalTime start_date;
    private final Long emplacement_pk_start;
    private final LocalTime end_date;
    private final Long emplacement_pk_end;
    private final boolean is_member;

    public Trip(LocalTime startDate, Long emplacementPkStart, LocalTime endDate, Long emplacementPkEnd, boolean isMember) {
        start_date = startDate;
        emplacement_pk_start = emplacementPkStart;
        end_date = endDate;
        emplacement_pk_end = emplacementPkEnd;
        is_member = isMember;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public Long getEmplacement_pk_start() {
        return emplacement_pk_start;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public Long getEmplacement_pk_end() {
        return emplacement_pk_end;
    }

    public boolean isIs_member() {
        return is_member;
    }
}
