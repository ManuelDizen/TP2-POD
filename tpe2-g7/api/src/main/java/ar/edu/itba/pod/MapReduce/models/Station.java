package ar.edu.itba.pod.MapReduce.models;

public class Station {
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

    public double getLatitude() {
        return location.latitude();
    }

    public double getLongitude() {
        return location.longitude();
    }
}
