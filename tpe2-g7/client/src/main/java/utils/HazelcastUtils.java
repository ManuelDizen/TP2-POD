package utils;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

import java.util.List;

public class HazelcastUtils {
    public static HazelcastInstance startHazelcast(List<String> addresses){
        // Client Config
        ClientConfig clientConfig = new ClientConfig();

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g7").setPassword("g7-pass");
        clientConfig.setGroupConfig(groupConfig);

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig()
                .setAddresses(addresses);

        clientConfig.setNetworkConfig(clientNetworkConfig);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
