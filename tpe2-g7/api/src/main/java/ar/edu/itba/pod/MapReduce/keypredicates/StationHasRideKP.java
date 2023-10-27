package ar.edu.itba.pod.MapReduce.keypredicates;

import ar.edu.itba.pod.MapReduce.models.Station;
import ar.edu.itba.pod.MapReduce.models.Trip;
import com.hazelcast.mapreduce.KeyPredicate;

import java.util.List;

public class StationHasRideKP implements KeyPredicate<Integer> {

    private final List<Trip> trips;

    public StationHasRideKP(List<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public boolean evaluate(Integer station) {
        for(Trip t : trips) {
            if(t.getEmplacement_pk_start().equals(station))
                return true;
        }
        return false;
    }
}
