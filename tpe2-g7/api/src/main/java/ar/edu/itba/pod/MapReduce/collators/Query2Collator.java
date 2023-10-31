package ar.edu.itba.pod.MapReduce.collators;

import ar.edu.itba.pod.MapReduce.models.Station;
import ar.edu.itba.pod.MapReduce.utils.Query2ReturnType;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Query2Collator implements Collator<Map.Entry<Long, Double>, List<Query2ReturnType>> {

    private final Map<Long, Station> stations;

    private final int n;

    public Query2Collator(Map<Long, Station> stations, int n) {
        this.stations = stations;
        this.n = n;
    }

    @Override
    public List<Query2ReturnType> collate(Iterable<Map.Entry<Long, Double>> iterable) {

        //OPCION 1: Sacrifico complejidad espacial
        List<Query2ReturnType> toReturn = new ArrayList<>();
        for(Map.Entry<Long, Double> entry : iterable) {
            Station station = stations.get(entry.getKey());
            Double value = entry.getValue();
            toReturn.add(new Query2ReturnType(station.getName(), Math.floor(value*100)/100));
        }
        toReturn.sort((o1, o2) -> {
            int c = (int) (o2.getAvg() - o1.getAvg());
            if (c != 0) return c;
            return o1.getName().compareTo(o2.getName());
        });

        return toReturn.subList(0, n);
    }
}
