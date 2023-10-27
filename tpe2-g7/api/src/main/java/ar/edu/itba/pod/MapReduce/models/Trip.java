package ar.edu.itba.pod.MapReduce.models;

import com.google.type.DateTime;

import java.time.LocalDateTime;

public class Trip {
    private final LocalDateTime start_date;
    private final Integer emplacement_pk_start;
    private final LocalDateTime end_date;
    private final Integer emplacement_pk_end;
    private final boolean is_member;

    public Trip(LocalDateTime startDate, Integer emplacementPkStart, LocalDateTime endDate, Integer emplacementPkEnd, boolean isMember) {
        start_date = startDate;
        emplacement_pk_start = emplacementPkStart;
        end_date = endDate;
        emplacement_pk_end = emplacementPkEnd;
        is_member = isMember;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public Integer getEmplacement_pk_start() {
        return emplacement_pk_start;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public Integer getEmplacement_pk_end() {
        return emplacement_pk_end;
    }

    public boolean isIs_member() {
        return is_member;
    }
}
