package ar.edu.itba.pod.MapReduce.collators;

import ar.edu.itba.pod.MapReduce.models.Afflux;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import ar.edu.itba.pod.MapReduce.utils.Query4ReturnType;
import com.hazelcast.client.impl.protocol.task.NoSuchMessageTask;
import com.hazelcast.mapreduce.Collator;

import java.awt.image.AreaAveragingScaleFilter;
import java.time.LocalDate;
import java.util.*;

public class Query4Collator implements Collator<Map.Entry<Pair<Long, LocalDate>, Integer>, List<Query4ReturnType>>{

    private final Map<Long, String> stations;
    private final int n;

    public Query4Collator(Map<Long, String> stations, int n){
        this.stations = stations;
        this.n = n;
    }

    @Override
    public List<Query4ReturnType> collate(Iterable<Map.Entry<Pair<Long, LocalDate>, Integer>> iterable) {
        List<Query4ReturnType> toReturn = new ArrayList<>();
        Map<Long, Afflux> auxMap = new HashMap<>();
        for(Map.Entry<Pair<Long, LocalDate>, Integer> entry : iterable){
            Long stationId = entry.getKey().getFirst();
            if(!auxMap.containsKey(stationId)){
                auxMap.put(stationId, new Afflux(0,0,0));
            }
            Integer val = entry.getValue();
            if (val == -1) {
                auxMap.get(stationId).incrementNegative();
            }
            else if(val == 0) {
                auxMap.get(stationId).incrementNeutral();
            }
            else if(val == 1) {
                auxMap.get(stationId).incrementPositive();
            }
            else{
                System.out.printf("Valor ilegal!!! es " + val);
            }
        }
        for(Map.Entry<Long, Afflux> entry : auxMap.entrySet()) {
            Afflux aff = entry.getValue();
            if(aff.getTotal() < n){
                aff.setNeutral(aff.getNeutral() + (n - aff.getTotal()));
            }
            toReturn.add(new Query4ReturnType(stations.get(entry.getKey()), entry.getValue()));
        }
        for(Map.Entry<Long, String> entry : stations.entrySet()){
            if(!auxMap.containsKey(entry.getKey())){
                toReturn.add(new Query4ReturnType(entry.getValue(), new Afflux(0,n,0)));
            }
        }
        toReturn.sort(Comparator.comparingInt(
                (Query4ReturnType o) -> o.getAfflux().getPositive()*-1)
                .thenComparing(Query4ReturnType::getStation));
        return toReturn;
    }
}
