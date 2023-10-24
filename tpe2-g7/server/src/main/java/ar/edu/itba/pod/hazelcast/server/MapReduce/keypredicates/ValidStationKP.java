package ar.edu.itba.pod.hazelcast.server.MapReduce.keypredicates;

import ar.edu.itba.pod.hazelcast.server.MapReduce.models.Trip;
import com.hazelcast.mapreduce.KeyPredicate;

import java.util.List;
import java.util.Objects;

public class ValidStationKP implements KeyPredicate<Trip>{

    private final List<Integer> stationIds;

    public ValidStationKP(List<Integer> stationIds) {
        this.stationIds = stationIds;
    }

    @Override
    public boolean evaluate(Trip trip) {
        //No es roundtrip, y ambas estaciones existen
        return !Objects.equals(trip.getEmplacement_pk_end(), trip.getEmplacement_pk_start())
                && stationIds.contains(trip.getEmplacement_pk_end())
                && stationIds.contains(trip.getEmplacement_pk_start());
    }
}
