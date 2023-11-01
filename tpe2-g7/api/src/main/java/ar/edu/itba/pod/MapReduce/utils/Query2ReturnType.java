package ar.edu.itba.pod.MapReduce.utils;

import com.hazelcast.mapreduce.Collator;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Query2ReturnType implements Serializable {

    private final String name;

    private final double avg;

    public Query2ReturnType(String name, double avg) {
        this.name = name;
        this.avg = avg;
    }

    public String getName() {
        return name;
    }

    public double getAvg() {
        return avg;
    }
}

