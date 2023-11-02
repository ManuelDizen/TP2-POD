package ar.edu.itba.pod.hazelcast.client;

import ar.edu.itba.pod.MapReduce.collators.Query2Collator;
import ar.edu.itba.pod.MapReduce.combiners.Query2CombinerFactory;
import ar.edu.itba.pod.MapReduce.mappers.Query2Mapper;
import ar.edu.itba.pod.MapReduce.models.Station;
import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.reducers.Query2ReducerFactory;
import ar.edu.itba.pod.MapReduce.utils.Query2ReturnType;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import static utils.ParsingUtils.*;

public class ClientQuery2 {

    private static final Logger logger = LoggerFactory.getLogger(ClientQuery2.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        logger.info("hz-config Client Starting ...");

        ParamsModel paramsModel;
        try {
            paramsModel = new ParamsModel();
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            HazelcastClient.shutdownAll();
            return;
        }

        int n;
        try {
            n = Integer.parseInt(ParsingUtils.getSystemProperty(PropertyNames.N).orElseThrow());
        }
        catch(NoSuchElementException | NumberFormatException f) {
            throw new InvalidParameterException("Invalid parameters. Now exiting");
        }

        final LogFileUtils logFile = new LogFileUtils("time2.txt", paramsModel.getOutPath(), Thread.currentThread().getStackTrace()[1].getMethodName(),
                ClientQuery2.class.getSimpleName());

        logger.info("Starting hazelcast instance");
        HazelcastInstance hazelcastInstance = HazelcastUtils.startHazelcast(paramsModel.getAddresses());
        logger.info("Hazelcast instance initiated");

        logFile.writeTimestampsLogger(String.valueOf(Thread.currentThread().getStackTrace()[1].getLineNumber()),
                "Inicio de la lectura del archivo");

        IMap<Long, Trip> trips = hazelcastInstance.getMap("trips");
        populateTrips(trips, paramsModel.getTripsPath());
        //fillBikesIMap(trips, paramsModel.getTripsPath()

        Map<Long, Station> stations = getStationLocationsFromCSV(paramsModel.getStationsPath());

        logFile.writeTimestampsLogger(String.valueOf(Thread.currentThread().getStackTrace()[1].getLineNumber()),
                "Fin de la lectura del archivo");

        final KeyValueSource<Long, Trip> KVSource = KeyValueSource.fromMap(trips);
        Job<Long, Trip> job = hazelcastInstance.getJobTracker("g7-q2").newJob(KVSource);

        logger.info("Starting MapReduce query...");
        logFile.writeTimestampsLogger(String.valueOf(Thread.currentThread().getStackTrace()[1].getLineNumber()),
                "Inicio del trabajo map/reduce");

        List<Query2ReturnType> result = job
                .mapper(new Query2Mapper(stations))
                .combiner(new Query2CombinerFactory())
                .reducer(new Query2ReducerFactory())
                .submit(new Query2Collator(stations, n))
                .get();

        ParsingUtils.Query2OutputParser(result, paramsModel.getOutPath());

        logFile.writeTimestampsLogger(String.valueOf(Thread.currentThread().getStackTrace()[1].getLineNumber()),
                "Fin del trabajo map/reduce");
        logger.info("Ending MapReduce query...");

        //Shutdown
        HazelcastClient.shutdownAll();
    }
}
