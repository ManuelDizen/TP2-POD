package ar.edu.itba.pod.MapReduce.models;

import com.google.type.DateTime;

import java.time.LocalTime;

public class Trip {
    private final LocalTime start_date;
    private final Integer emplacement_pk_start;
    private final LocalTime end_date;
    private final Integer emplacement_pk_end;
    private final boolean is_member;

    public Trip(LocalTime startDate, Integer emplacementPkStart, LocalTime endDate, Integer emplacementPkEnd, boolean isMember) {
        start_date = startDate;
        emplacement_pk_start = emplacementPkStart;
        end_date = endDate;
        emplacement_pk_end = emplacementPkEnd;
        is_member = isMember;
    }

    public LocalTime getStart_date() {
        return start_date;
    }

    public Integer getEmplacement_pk_start() {
        return emplacement_pk_start;
    }

    public LocalTime getEnd_date() {
        return end_date;
    }

    public Integer getEmplacement_pk_end() {
        return emplacement_pk_end;
    }

    public boolean isIs_member() {
        return is_member;
    }
}
