package ar.edu.itba.pod.hazelcast.client;

import ar.edu.itba.pod.MapReduce.collators.Query1Collator;
import ar.edu.itba.pod.MapReduce.combiners.Query1CombinerFactory;
import ar.edu.itba.pod.MapReduce.mappers.Query1Mapper;
import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.reducers.Query1ReducerFactory;
import ar.edu.itba.pod.MapReduce.utils.Query1ReturnType;
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

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static utils.ParsingUtils.*;

public class ClientQuery1 {
    private static final Logger logger = LoggerFactory.getLogger(ClientQuery1.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        logger.info("hz-config Client Starting ...");

        ParamsModel paramsModel;
        try {
            paramsModel = new ParamsModel();
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            return;
        }

        logger.info("Starting hazelcast instance");
        HazelcastInstance hazelcastInstance = HazelcastUtils.startHazelcast(paramsModel.getAddresses());
        logger.info("Hazelcast instance initiated");

        IMap<Long, Trip> trips = hazelcastInstance.getMap("trips");
        populateTrips(trips, paramsModel.getTripsPath());

        Map<Long, String> stations = getStationsFromCSV(paramsModel.getStationsPath());

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
        ParsingUtils.Query1OutputParser(result, paramsModel.getOutPath());

        //TODO: Timestamps para medir métricas

        //Shutdown
        HazelcastClient.shutdownAll();
    }
}