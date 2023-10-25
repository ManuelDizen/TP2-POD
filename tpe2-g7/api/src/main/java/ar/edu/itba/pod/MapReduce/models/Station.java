package ar.edu.itba.pod.MapReduce.models;

public class Station {
    private final Integer pk;
    private final String name;
    private final double latitude;
    private final double longitude;

    public Station(Integer pk, String name, double latitude, double longitude) {
        this.pk = pk;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getPk() {
        return pk;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
