package ar.edu.itba.pod.hazelcast.client;

import ar.edu.itba.pod.MapReduce.collators.Query1Collator;
import ar.edu.itba.pod.MapReduce.combiners.Query1CombinerFactory;
import ar.edu.itba.pod.MapReduce.keypredicates.ValidStationKP;
import ar.edu.itba.pod.MapReduce.mappers.Query1Mapper;
import ar.edu.itba.pod.MapReduce.models.Station;
import ar.edu.itba.pod.MapReduce.models.Trip;
import ar.edu.itba.pod.MapReduce.reducers.Query1ReducerFactory;
import ar.edu.itba.pod.MapReduce.utils.Pair;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ParsingUtils;
import utils.PropertyNames;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static utils.ParsingUtils.*;

public class ClientQuery1 {
    private static final Logger logger = LoggerFactory.getLogger(ClientQuery1.class);

    public static void main(String[] args) {
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

        // Client Config
        ClientConfig clientConfig = new ClientConfig();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g3").setPassword("g3-pass");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig()
                .setAddresses(List.of("172.31.115.255:5701"));
        //String[] addresses = {"192.168.1.51:5701"};
        //clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

        IMap<Long, Trip> trips = hazelcastInstance.getMap("trips");
        populateTrips(trips, tripsPath);

        Map<Long, String> stations = getStationsFromCSV(stationsPath);


        final KeyValueSource<Long, Trip> KVSource = KeyValueSource.fromMap(trips);
        Job<Long, Trip> job = hazelcastInstance.getJobTracker("g7-q1").newJob(KVSource);

        /*List<Map.Entry<Pair<String>, Long>> result = job
                .keyPredicate(new ValidStationKP(stations))
                .mapper(new Query1Mapper())
                .combiner(new Query1CombinerFactory())
                .reducer(new Query1ReducerFactory())
                .submit(new Query1Collator(stations));
        */
        //TODO: Hay una cosa que esta mal pensada, que es el que el KP recibe la key de los pares,
        // por lo que tengo que cambiar la logica inicial del KP y el mapper. El resto ya estaria.
        //Shutdown
        HazelcastClient.shutdownAll();
    }
}