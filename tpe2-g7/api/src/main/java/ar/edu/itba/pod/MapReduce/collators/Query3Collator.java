package ar.edu.itba.pod.MapReduce.collators;

import ar.edu.itba.pod.MapReduce.models.Ride;
import ar.edu.itba.pod.MapReduce.utils.Query3ReturnType;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Query3Collator implements Collator<Map.Entry<Long, Ride>, List<Query3ReturnType>>{

    private final Map<Long, String> stations;

    public Query3Collator(Map<Long, String> stations){
        this.stations = stations;
    }
    @Override
    public List<Query3ReturnType> collate(Iterable<Map.Entry<Long, Ride>> iterable) {
        List<Query3ReturnType> toReturn = new ArrayList<>();
        for(Map.Entry<Long, Ride> entry : iterable){
            Long key = entry.getKey();
            Ride value = entry.getValue();
            String name1 = stations.get(key);
            String name2 = stations.get(value.getEmplacement_pk_end());
            toReturn.add(new Query3ReturnType(
                    name1, name2, value.getStart_date(), value.getMinutes()
            ));
        }
        toReturn.sort((o1, o2) -> {
            int minDiff = o2.getMinutes() - o1.getMinutes();
            if (minDiff != 0) return minDiff;
            return o1.getStart().toLowerCase().compareTo(o2.getStart().toLowerCase());
        });
        return toReturn;
    }
}
