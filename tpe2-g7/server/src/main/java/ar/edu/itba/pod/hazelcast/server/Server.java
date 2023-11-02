package ar.edu.itba.pod.hazelcast.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("hz-config Server Starting ...");
        // Config
        Config config = new Config();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g7").setPassword("g7-pass");
        config.setGroupConfig(groupConfig);


        // Network Config
        MulticastConfig multicastConfig = new MulticastConfig();

        JoinConfig joinConfig = new JoinConfig().setMulticastConfig(multicastConfig);
        
        InterfacesConfig interfacesConfig = new InterfacesConfig().
                setInterfaces(List.of("192.168.0.*"))
                .setEnabled(false);

        NetworkConfig networkConfig = new NetworkConfig()
                .setInterfaces(interfacesConfig)
                .setJoin(joinConfig);

        config.setNetworkConfig(networkConfig);

        // Start cluster
        Hazelcast.newHazelcastInstance(config);
    }
}
