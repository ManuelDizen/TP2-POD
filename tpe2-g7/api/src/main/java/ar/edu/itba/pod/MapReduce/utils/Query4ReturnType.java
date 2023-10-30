package ar.edu.itba.pod.MapReduce.utils;

import ar.edu.itba.pod.MapReduce.models.Afflux;

public class Query4ReturnType {
    private final String station;
    private final Afflux afflux;

    public Query4ReturnType(String station, Afflux afflux) {
        this.station = station;
        this.afflux = afflux;
    }

    public String getStation() {
        return station;
    }

    public Afflux getAfflux() {
        return afflux;
    }
}
