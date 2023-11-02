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
        List<Query2ReturnType> auxList = new ArrayList<>();
        for(Map.Entry<Long, Double> entry : iterable) {
            Station station = stations.get(entry.getKey());
            Double value = entry.getValue();
            auxList.add(new Query2ReturnType(station.getName(), Math.floor(value*100)/100));
        }
        auxList.sort((o1, o2) -> {
            double c = o2.getAvg() - o1.getAvg();
            if (c != 0) return c>0?1:-1;
            return o1.getName().compareTo(o2.getName());
        });
        List<Query2ReturnType> toReturn = new ArrayList<>();
        for(int i = 0; i < auxList.size(); i++){
            // Nota al lector (?): Inicialmente se uso el metodo clasico sublist(from, to), pero
            // el codigo fallaba por culpa de casteos de tipos internos de java. Es por ello que es un
            // sublsit "casero"
            if(i < n) {
                toReturn.add(auxList.get(i));
            }
            else
                break;
        }
        return toReturn;
    }
}
