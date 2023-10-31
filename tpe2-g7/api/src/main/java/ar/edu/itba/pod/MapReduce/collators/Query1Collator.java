package ar.edu.itba.pod.MapReduce.collators;

import ar.edu.itba.pod.MapReduce.utils.Pair;
import ar.edu.itba.pod.MapReduce.utils.Query1ReturnType;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class Query1Collator implements Collator<Map.Entry<Pair<Long, Long>, Long>, List<Query1ReturnType>>{

    private final Map<Long, String> stations;

    public Query1Collator(Map<Long, String> stations){
        this.stations = stations;
    }
    @Override
    public List<Query1ReturnType> collate(Iterable<Map.Entry<Pair<Long, Long>, Long>> iterable) {

        List<Query1ReturnType> toReturn = new ArrayList<>();
        for(Map.Entry<Pair<Long, Long>, Long> entry : iterable){
            Pair<Long, Long> key = entry.getKey();
            String name1 = stations.get(key.getFirst());
            String name2 = stations.get(key.getSecond());
            toReturn.add(new Query1ReturnType(
                    name1, name2, entry.getValue()
            ));
        }
        toReturn.sort((o1, o2) -> {
            int tripDiff = (int) (o2.getTrips() - o1.getTrips());
            if (tripDiff != 0) return tripDiff;
            int nameDiff = o1.getFrom().compareTo(o2.getFrom());
            if (nameDiff != 0) return nameDiff;
            return o1.getTo().compareTo(o2.getTo());
        });
        return toReturn;
    }
}
