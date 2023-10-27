package ar.edu.itba.pod.MapReduce.keypredicates;

import ar.edu.itba.pod.MapReduce.models.Trip;
import com.hazelcast.mapreduce.KeyPredicate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ValidStationKP implements KeyPredicate<Trip>{

    private final Set<Long> stationIds;

    public ValidStationKP(Map<Long, String> stationIds) {
        this.stationIds = stationIds.keySet();
    }

    @Override
    public boolean evaluate(Trip trip) {
        //No es roundtrip, y ambas estaciones existen
        return !Objects.equals(trip.getEmplacement_pk_end(), trip.getEmplacement_pk_start())
                && stationIds.contains(trip.getEmplacement_pk_end())
                && stationIds.contains(trip.getEmplacement_pk_start());
    }
}
