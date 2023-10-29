package ar.edu.itba.pod.hazelcast.client;

import ar.edu.itba.pod.MapReduce.collators.Query1Collator;
import ar.edu.itba.pod.MapReduce.combiners.Query1CombinerFactory;
import ar.edu.itba.pod.MapReduce.keypredicates.ValidStationKP;
import ar.edu.itba.pod.MapReduce.mappers.Query1Mapper;
import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.reducers.Query1ReducerFactory;
import ar.edu.itba.pod.MapReduce.utils.Query1ReturnType;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.HazelcastUtils;
import utils.ParsingUtils;
import utils.PropertyNames;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import static utils.ParsingUtils.*;

public class ClientQuery1 {
    private static final Logger logger = LoggerFactory.getLogger(ClientQuery1.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        logger.info("hz-config Client Starting ...");

        //TODO:Paramterizar todo el parseo de argumentos como el tp1
        String aux;
        String inPath;
        String outPath;
        try{
            aux = ParsingUtils.getSystemProperty(PropertyNames.ADDRESSES).orElseThrow();
            inPath = ParsingUtils.getSystemProperty(PropertyNames.IN_PATH).orElseThrow();
            outPath = ParsingUtils.getSystemProperty(PropertyNames.OUT_PATH).orElseThrow();
        }
        catch(NoSuchElementException | NumberFormatException f) {
            throw new InvalidParameterException("Invalid parameters. Now exiting");
        }

        List<String> addresses = Arrays.asList(aux.split(";"));
        String tripsPath = inPath + "/bikes.csv";
        String stationsPath = inPath + "/stations.csv";

        logger.info("Starting hazelcast instance");
        HazelcastInstance hazelcastInstance = HazelcastUtils.startHazelcast(addresses);
        logger.info("Hazelcast instance initiated");

        IMap<Long, Trip> trips = hazelcastInstance.getMap("trips");
        populateTrips(trips, tripsPath);

        Map<Long, String> stations = getStationsFromCSV(stationsPath);


        final KeyValueSource<Long, Trip> KVSource = KeyValueSource.fromMap(trips);
        Job<Long, Trip> job = hazelcastInstance.getJobTracker("g7-q1").newJob(KVSource);

        logger.info("Starting MapReduce query...");
        List<Query1ReturnType> result = job
                .mapper(new Query1Mapper(stations.keySet()))
                .combiner(new Query1CombinerFactory())
                .reducer(new Query1ReducerFactory())
                .submit(new Query1Collator(stations))
                .get();
        logger.info("Ending MapReduce query...");
        ParsingUtils.Query1OutputParser(result, outPath);

        //TODO: Timestamps para medir métricas

        //Shutdown
        HazelcastClient.shutdownAll();
    }
}