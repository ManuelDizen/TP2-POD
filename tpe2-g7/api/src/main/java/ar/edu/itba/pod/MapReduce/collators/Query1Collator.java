package ar.edu.itba.pod.MapReduce.collators;

import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Query1Collator implements Collator<Map.Entry<Pair<Integer>, Long>, List<Map.Entry<Pair<String>, Long>>>{

    private final Map<Long, String> stations;

    public Query1Collator(Map<Long, String> stations){
        this.stations = stations;
    }
    @Override
    public List<Map.Entry<Pair<String>, Long>> collate(Iterable<Map.Entry<Pair<Integer>, Long>> iterable) {
        List<Map.Entry<Pair<String>, Long>> toReturn = new ArrayList<>();
        //TODO lo dejo pendiente, redacto el problema.
        // Por ahora, aca llega con entradas de tipo: id salida, id llegada, cant viajes
        // Aca necesito cambiar los ids por los nombres, y despues ordenar por:
        // - Descendente cantidad total
        // - Alfabetico nombre de A
        // - Alfabetico nombre de B




        return null;
    }
}
