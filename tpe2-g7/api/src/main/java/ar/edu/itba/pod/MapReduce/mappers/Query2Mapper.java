package ar.edu.itba.pod.MapReduce.mappers;

import ar.edu.itba.pod.MapReduce.models.Location;
import ar.edu.itba.pod.MapReduce.models.Station;
import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Query2Mapper implements Mapper<Long, Trip, Long, Pair<Double, Long>> {


    private static final Long ONE = 1L;
    private final Map<Long, Station> stations;

    public Query2Mapper(Map<Long, Station> stations) {
        this.stations = new HashMap<>(stations);
    }

    private Double distance(Location l1, Location l2) {

        //https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/

        // distance between latitudes and longitudes
        double dLat = Math.toRadians(l2.getLatitude() - l1.getLatitude());
        double dLon = Math.toRadians(l2.getLongitude() - l1.getLongitude());

        // convert to radians
        double lat1 = Math.toRadians(l1.getLatitude());
        double lat2 = Math.toRadians(l2.getLatitude());

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    @Override
    public void map(Long key, Trip trip, Context<Long, Pair<Double, Long>> context) {
        //la idea es, por cada viaje, calcular la distancia recorrida y emitir una key
        //con nombre de estación de origen y kms del viaje
        if(trip.is_member()) {
            Long start = trip.getEmplacement_pk_start();
            Long end = trip.getEmplacement_pk_end();
            if(!Objects.equals(start, end) && stations.containsKey(start)
                    && stations.containsKey(end)) {

                Pair<Double, Long> pair = new Pair<>(distance(stations.get(start).getLocation(),
                        stations.get(end).getLocation()), ONE);

                context.emit(start, pair);
            }
        }
    }
}
