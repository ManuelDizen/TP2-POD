package utils;

import ar.edu.itba.pod.MapReduce.models.Location;
import ar.edu.itba.pod.MapReduce.models.Station;
import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.utils.Query1ReturnType;
import ar.edu.itba.pod.MapReduce.utils.Query3ReturnType;
import ar.edu.itba.pod.MapReduce.utils.Query4ReturnType;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
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

    public static List<String[]> parseCsvFirstNEntries(String path, int n){
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

        int i = 0;
        for (CSVRecord record : parser) {
            if(i==n) break;
            if (skipFirstLine) {
                skipFirstLine = false;
                continue;
            }
            String[] tokenizedArray = record.get(0).split(";");
            lines.add(tokenizedArray);
            i++;
        }
        return lines;
    }

    public static void populateTrips(IMap<Long, Trip> trips, String tripsPath){
        long ctr = 1L;
        //List<String[]> tripsCSV = ParsingUtils.parseCsv(tripsPath);
        List<String[]> tripsCSV = ParsingUtils.parseCsvFirstNEntries(tripsPath, 10000);
        for (String[] bike : tripsCSV) {
            trips.put(ctr++, new Trip(
                    LocalDateTime.parse(bike[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    Long.parseLong(bike[1]),
                    LocalDateTime.parse(bike[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    Long.parseLong(bike[3]),
                    Boolean.parseBoolean(bike[4])
            ));
        }
    }

    public static void populateStations(IList<Station> stations, String stationsPath){
        List<String[]> stationsCSV = ParsingUtils.parseCsv(stationsPath);
        for (String[] station : stationsCSV) {
            stations.add(new Station(
                    Long.parseLong(station[0]),
                    station[1],
                    Double.parseDouble(station[2]),
                    Double.parseDouble(station[3])
            ));
        }
    }

    public static void populateCollections(IMap<Long, Trip> trips, IList<Station> stations,
                                           String tripsPath, String stationsPath){
        populateTrips(trips, tripsPath);
        populateStations(stations, stationsPath);
    }

    public static Map<Long, String> getStationsFromCSV(String path){
        Map<Long, String> toReturn = new HashMap<>();
        List<String[]> stationsCSV = ParsingUtils.parseCsv(path);
        for (String[] station : stationsCSV) {
            toReturn.put(Long.parseLong(station[0]), station[1]);
        }
        return toReturn;
    }

    public static Map<Long, Station> getStationLocationsFromCSV(String path){
        Map<Long, Station> toReturn = new HashMap<>();
        List<String[]> stationsCSV = ParsingUtils.parseCsv(path);
        for (String[] station : stationsCSV) {
            toReturn.put(Long.parseLong(station[0]), new Station(Long.parseLong(station[0]), station[1], Double.parseDouble(station[2]), Double.parseDouble(station[3])));
        }
        return toReturn;
    }

    public static void Query1OutputParser(List<Query1ReturnType> dataList, String outPath) {
        try (FileWriter writer = new FileWriter(outPath + "/query1.csv")) {
            writer.append("station_a;station_b;trips_between_a_b\n");
            for (Query1ReturnType data : dataList) {
                writer.append(data.getFrom()).append(';')
                        .append(data.getTo()).append(';')
                        .append(String.valueOf(data.getTrips()))
                        .append('\n');
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void Query3OutputParser(List<Query3ReturnType> dataList, String outPath) {
        try (FileWriter writer = new FileWriter(outPath + "/query3.csv")) {
            writer.append("start_station;end_station;start_date;minutes\n");
            DateTimeFormatter f = DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm:ss" ) ;
            for (Query3ReturnType data : dataList) {
                writer.append(data.getStart()).append(';')
                        .append(data.getEnd()).append(';')
                        .append(data.getDate().format(f)).append(';')
                        .append(String.valueOf(data.getMinutes()))
                        .append('\n');
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static void Query4OutputParser(List<Query4ReturnType> dataList, String outPath) {
        try (FileWriter writer = new FileWriter(outPath + "/query4.csv")) {
            writer.append("station;pos_afflux;neutral_afflux;negative_afflux\n");
            for (Query4ReturnType data : dataList) {
                writer.append(data.getStation()).append(';')
                        .append(String.valueOf(data.getAfflux().getPositive())).append(';')
                        .append(String.valueOf(data.getAfflux().getNeutral())).append(';')
                        .append(String.valueOf(String.valueOf(data.getAfflux().getNegative())))
                        .append('\n');
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
