package ar.edu.itba.pod.hazelcast.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobTracker;

import models.Station;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ParsingUtils;
import utils.PropertyNames;

import java.security.InvalidParameterException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

//TODO: donde poner los models patra usarlos en el client
import models.Trip;

public class Client2 {
    private static final Logger logger = LoggerFactory.getLogger(Client2.class);

    public static void main(String[] args) {

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
        String bikesPath = inPath + "/bikes.csv";
        String stationsPath = inPath + "/stations.csv";


        logger.info("hz-config Client Starting ...");
        // Client Config
        ClientConfig clientConfig = new ClientConfig();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g7").setPassword("g7-pass");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig().setAddresses(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

        logger.info("Inicio de la lectura de los archivos");

        //TODO: ME QUIERO QUEDAR CON LA DATA DE LOS CSV --> YA TENGO LOS MODELS DE TRIP Y STATION
        //USAR ILIST (LISTA DE HAZELCAST)
        IList<Trip> bikes = hazelcastInstance.getList("bikes");
        List<String[]> bikesCSV = ParsingUtils.parseCsv(bikesPath);
        //TODO: Files.line ??
        for (String[] bike : bikesCSV) {
            bikes.add(new Trip(
                    LocalTime.parse(bike[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    Integer.parseInt(bike[1]),
                    LocalTime.parse(bike[2], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    Integer.parseInt(bike[3]),
                    Boolean.parseBoolean(bike[4])
            ));
        }

        IList<Station> stations = hazelcastInstance.getList("stations");
        List<String[]> stationsCSV = ParsingUtils.parseCsv(stationsPath);
        //TODO: Files.line ??
        for (String[] station : stationsCSV) {
            stations.add(new Station(
                    Integer.parseInt(station[0]),
                    station[1],
                    Double.parseDouble(station[2]),
                    Double.parseDouble(station[3])
            ));
        }

        logger.info("Fin de la lectura de los archivos");

        //TODO: que es el job??
        JobTracker tracker = hazelcastInstance.getJobTracker("query1");


        String mapName = "testMap";
        IMap<Integer, String> testMapFromMember = hazelcastInstance.getMap(mapName);
        testMapFromMember.set(1, "test1");
        IMap<Integer, String> testMap = hazelcastInstance.getMap(mapName);
        System.out.println(testMap.get(1));
        //Shutdown
        HazelcastClient.shutdownAll();
    }
}