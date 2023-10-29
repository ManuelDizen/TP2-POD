package ar.edu.itba.pod.MapReduce.mappers;

import ar.edu.itba.pod.MapReduce.models.Location;
import ar.edu.itba.pod.MapReduce.models.Station;
import ar.edu.itba.pod.MapReduce.models.Trip;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query2Mapper implements Mapper<Long, Trip, Long, Double> {

    private final Map<Long, Location> stations = new HashMap<>();

    public Query2Mapper(List<Station> stations) {
        for(Station station : stations) {
            this.stations.put(station.getPk(), new Location(station.getLatitude(), station.getLongitude()));
        }
    }

    private Double distance(Location l1, Location l2) {

        //https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/

        // distance between latitudes and longitudes
        double dLat = Math.toRadians(l2.latitude() - l1.latitude());
        double dLon = Math.toRadians(l2.longitude() - l1.longitude());

        // convert to radians
        double lat1 = Math.toRadians(l1.latitude());
        double lat2 = Math.toRadians(l2.latitude());

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
    public void map(Long key, Trip trip, Context<Long, Double> context) {
        //la idea es, por cada viaje, calcular la distancia recorrida y emitir una key
        //con nombre de estación de origen y kms del viaje
        Long start = trip.getEmplacement_pk_start();
        Long end = trip.getEmplacement_pk_end();
        Double dist = distance(stations.get(start), stations.get(end));
        context.emit(start, dist);
    }
}
