package ar.edu.itba.pod.MapReduce.collators;

import ar.edu.itba.pod.MapReduce.models.Afflux;
import ar.edu.itba.pod.MapReduce.utils.Query4ReturnType;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Query4Collator implements Collator<Map.Entry<Long, Afflux>, List<Query4ReturnType>>{

    private final Map<Long, String> stations;

    public Query4Collator(Map<Long, String> stations){
        this.stations = stations;
    }
    @Override
    public List<Query4ReturnType> collate(Iterable<Map.Entry<Long, Afflux>> iterable) {

        List<Query4ReturnType> toReturn = new ArrayList<>();
        for(Map.Entry<Long, Afflux> entry : iterable){
            Long key = entry.getKey();
            Afflux value = entry.getValue();
            String name = stations.get(key);
            toReturn.add(new Query4ReturnType(name, value));
        }
        toReturn.sort((o1, o2) -> {
            int affluxDiff = o2.getAfflux().getPositive() - o1.getAfflux().getPositive();
            if (affluxDiff != 0) return affluxDiff;
            return o1.getStation().compareTo(o2.getStation());
        });
        return toReturn;
    }
}
