package ar.edu.itba.pod.MapReduce.models;

import java.io.Serializable;

public class Station implements Serializable {
    private final Long pk;
    private final String name;
    private final Location location;

    public Station(Long pk, String name, double latitude, double longitude) {
        this.pk = pk;
        this.name = name;
        this.location = new Location(latitude, longitude);
    }

    public Long getPk() {
        return pk;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() { return location; }
}
