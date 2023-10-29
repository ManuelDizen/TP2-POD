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
import java.util.Map;
import java.util.NoSuchElementException;

import static utils.ParsingUtils.getStationsFromCSV;
import static utils.ParsingUtils.populateTrips;

public class ClientQuery2 {

    private static final Logger logger = LoggerFactory.getLogger(ClientQuery1.class);

    public static void main(String[] args) {
        logger.info("hz-config Client Starting ...");

        ParamsModel paramsModel;
        try {
            paramsModel = new ParamsModel();
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            return;
        }
        int n;
        try{
            n = Integer.parseInt(ParsingUtils.getSystemProperty(PropertyNames.N).orElseThrow());
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
        //TODO: PASS MAPPER TO JOB
        logger.info("Ending MapReduce query...");

        String mapName = "testMap";
        IMap<Integer, String> testMapFromMember = hazelcastInstance.getMap(mapName);
        testMapFromMember.set(1, "test1");
        IMap<Integer, String> testMap = hazelcastInstance.getMap(mapName);
        System.out.println(testMap.get(1));

        //Shutdown
        HazelcastClient.shutdownAll();
    }
}
