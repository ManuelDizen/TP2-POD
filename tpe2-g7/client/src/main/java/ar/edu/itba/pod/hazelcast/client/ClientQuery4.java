package ar.edu.itba.pod.hazelcast.client;

import ar.edu.itba.pod.MapReduce.models.Trip;
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

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;

import static utils.ParsingUtils.getStationsFromCSV;
import static utils.ParsingUtils.populateTrips;

public class ClientQuery4 {
    private static final Logger logger = LoggerFactory.getLogger(ClientQuery4.class);

    public static void main(String[] args) {
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
            startDate = LocalDate.parse(ParsingUtils.getSystemProperty(PropertyNames.START_DATE).orElseThrow());
            endDate = LocalDate.parse(ParsingUtils.getSystemProperty(PropertyNames.END_DATE).orElseThrow());
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

        logger.info("Starting MapReduce query...");
        // PASSARLE AL JOB EL MAPPER Y EL REDUCER
        logger.info("Ending MapReduce query...");

        // WRITE TO OUTPUT

        HazelcastClient.shutdownAll();
    }
}