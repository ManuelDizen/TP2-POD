package ar.edu.itba.pod.hazelcast.client;

import ar.edu.itba.pod.MapReduce.collators.Query4Collator;
import ar.edu.itba.pod.MapReduce.combiners.Query4CombinerFactory;
import ar.edu.itba.pod.MapReduce.mappers.Query4Mapper;
import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.reducers.Query4ReducerFactory;
import ar.edu.itba.pod.MapReduce.utils.Query4ReturnType;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HazelcastUtils;
import utils.ParamsModel;
import utils.ParsingUtils;
import utils.PropertyNames;

import javax.management.Query;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static utils.ParsingUtils.getStationsFromCSV;
import static utils.ParsingUtils.populateTrips;

public class ClientQuery4 {
    private static final Logger logger = LoggerFactory.getLogger(ClientQuery4.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        logger.info("hz-config Client Starting ...");

        ParamsModel paramsModel;
        try {
            paramsModel = new ParamsModel();
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            return;
        }

        LocalDate startDate;
        LocalDate endDate;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            startDate = LocalDate.parse(ParsingUtils.getSystemProperty(PropertyNames.START_DATE).orElseThrow(), formatter);
            endDate = LocalDate.parse(ParsingUtils.getSystemProperty(PropertyNames.END_DATE).orElseThrow(), formatter);
            if(endDate.isBefore(startDate)){
                logger.error("enddate cannot be before start date. Exiting.");
                return;
            }
        }
        catch(NoSuchElementException | NumberFormatException f) {
            throw new InvalidParameterException("Invalid parameters. Now exiting");
        }

        logger.info("Starting hazelcast instance");
        HazelcastInstance hazelcastInstance = HazelcastUtils.startHazelcast(paramsModel.getAddresses());
        logger.info("Hazelcast instance initiated");

        IMap<Long, Trip> trips = hazelcastInstance.getMap("trips");
        populateTrips(trips, paramsModel.getTripsPath());

        Map<Long, String> stations = getStationsFromCSV(paramsModel.getStationsPath());

        final KeyValueSource<Long, Trip> KVSource = KeyValueSource.fromMap(trips);
        Job<Long, Trip> job = hazelcastInstance.getJobTracker("g7-q1").newJob(KVSource);

        int totalDays = (int) startDate.until(endDate, ChronoUnit.DAYS);

        logger.info("Starting MapReduce query...");
        List<Query4ReturnType> result = job
                .mapper(new Query4Mapper())
                .combiner(new Query4CombinerFactory())
                .reducer(new Query4ReducerFactory())
                .submit(new Query4Collator(stations, totalDays))
                .get();

        logger.info("Ending MapReduce query...");
        ParsingUtils.Query4OutputParser(result, paramsModel.getOutPath());

        //TODO: Timestamps para medir métricas

        HazelcastClient.shutdownAll();
    }
}