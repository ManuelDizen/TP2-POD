package utils;

import ar.edu.itba.pod.MapReduce.models.Station;
import ar.edu.itba.pod.MapReduce.models.Trip;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ParsingUtils {

    private static final Logger logger = LoggerFactory.getLogger(ParsingUtils.class);

    public static Optional<String> getSystemProperty(String name){
        final String prop = System.getProperty(name);
        if(prop == null){
            return Optional.empty();
        }
        return Optional.of(prop);
    }

    public static List<String[]> parseCsv(String path){
        FileReader file;
        try{
            file = new FileReader(path);
        }
        catch(FileNotFoundException e){
            throw new RuntimeException(e.getMessage());
        }

        CSVParser parser;
        try {
            parser = new CSVParser(file, CSVFormat.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean skipFirstLine = true;
        List<String[]> lines = new ArrayList<>();

        for (CSVRecord record : parser) {
            if (skipFirstLine) {
                skipFirstLine = false;
                continue;
            }
            String[] tokenizedArray = record.get(0).split(";");
            lines.add(tokenizedArray);
        }
        return lines;
    }

    public static void populateTrips(IMap<Long, Trip> trips, String tripsPath){
        long ctr = 1L;
        List<String[]> tripsCSV = ParsingUtils.parseCsv(tripsPath);
        for (String[] bike : tripsCSV) {
            trips.put(ctr++, new Trip(
                    LocalTime.parse(bike[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    Long.parseLong(bike[1]),
                    LocalTime.parse(bike[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    Long.parseLong(bike[3]),
                    Boolean.parseBoolean(bike[4])
            ));
        }
    }
    public static void populateCollections(IMap<Long, Trip> trips, IList<Station> stations,
                                           String tripsPath, String stationsPath){
        populateTrips(trips, tripsPath);

        List<String[]> stationsCSV = ParsingUtils.parseCsv(stationsPath);
        for (String[] station : stationsCSV) {
            stations.add(new Station(
                    Integer.parseInt(station[0]),
                    station[1],
                    Double.parseDouble(station[2]),
                    Double.parseDouble(station[3])
            ));
        }
    }

    public static Map<Long, String> getStationsFromCSV(String path){
        Map<Long, String> toReturn = new HashMap<>();
        List<String[]> stationsCSV = ParsingUtils.parseCsv(path);
        for (String[] station : stationsCSV) {
            toReturn.put(Long.parseLong(station[0]), station[1]);
        }
        return toReturn;
    }

}
