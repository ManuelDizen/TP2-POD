package ar.edu.itba.pod.hazelcast.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClientQuery1 {
    private static final Logger logger = LoggerFactory.getLogger(ClientQuery1.class);

    public static void main(String[] args) {
        logger.info("hz-config Client Starting ...");
        // Client Config
        ClientConfig clientConfig = new ClientConfig();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g7").setPassword("g7-pass");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig()
                .setAddresses(List.of("127.0.0.1:5701"));
        //String[] addresses = {"192.168.1.51:5701"};
        //clientNetworkConfig.addAddress(addresses);
        clientConfig.setNetworkConfig(clientNetworkConfig);
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

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