package ar.edu.itba.pod.MapReduce.utils;

import java.io.Serializable;

public class Query1ReturnType implements Serializable {
    private final String from;
    private final String to;
    private final Long trips;


    public Query1ReturnType(String from, String s, Long trips) {
        this.from = from;
        to = s;
        this.trips = trips;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Long getTrips() {
        return trips;
    }
}
