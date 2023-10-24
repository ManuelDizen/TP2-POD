package ar.edu.itba.pod.hazelcast.server.MapReduce.models;

import com.google.type.DateTime;

public class Trip {
    private final DateTime start_date;
    private final Integer emplacement_pk_start;
    private final DateTime end_date;
    private final Integer emplacement_pk_end;
    private final boolean is_member;

    public Trip(DateTime startDate, Integer emplacementPkStart, DateTime endDate, Integer emplacementPkEnd, boolean isMember) {
        start_date = startDate;
        emplacement_pk_start = emplacementPkStart;
        end_date = endDate;
        emplacement_pk_end = emplacementPkEnd;
        is_member = isMember;
    }

    public DateTime getStart_date() {
        return start_date;
    }

    public Integer getEmplacement_pk_start() {
        return emplacement_pk_start;
    }

    public DateTime getEnd_date() {
        return end_date;
    }

    public Integer getEmplacement_pk_end() {
        return emplacement_pk_end;
    }

    public boolean isIs_member() {
        return is_member;
    }
}
