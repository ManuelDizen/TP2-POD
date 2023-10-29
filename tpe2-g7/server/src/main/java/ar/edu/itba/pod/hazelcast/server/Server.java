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

        System.out.println("ANTES DE PASAR POR INTERFACESCONFIG");

        InterfacesConfig interfacesConfig = new InterfacesConfig().
                setInterfaces(List.of("192.168.1.*"))
                .setEnabled(true);

        System.out.println("DESPUES DE PASAR");

        NetworkConfig networkConfig = new NetworkConfig()
                .setInterfaces(interfacesConfig)
                .setJoin(joinConfig);

        config.setNetworkConfig(networkConfig);

        // Management Center Config
        //ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig().setUrl("http://localhost:8080/mancenter/").setEnabled(true);
        //config.setManagementCenterConfig(managementCenterConfig);

        // Start cluster
        Hazelcast.newHazelcastInstance(config);
    }
}
