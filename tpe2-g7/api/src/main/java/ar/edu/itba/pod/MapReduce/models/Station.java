package ar.edu.itba.pod.MapReduce.models;

public class Station {
    private final Integer pk;
    private final String name;
    private final Location location;

    public Station(Integer pk, String name, double latitude, double longitude) {
        this.pk = pk;
        this.name = name;
        this.location = new Location(latitude, longitude);
    }

    public Integer getPk() {
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
