package com.project.tchokonthe.config;


import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

import static com.hazelcast.config.EvictionPolicy.LRU;
import static com.hazelcast.config.MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE;
import static com.hazelcast.core.Hazelcast.newHazelcastInstance;

@Configuration
@EnableCaching
public class HazelcastConfig {


    private static final Logger logger = LoggerFactory.getLogger(HazelcastConfig.class);


    @Bean
    public Config hazelCastConfig() {

        ManagementCenterConfig mcc = new ManagementCenterConfig()
                .setUrl("http://192.168.99.100:38080/mancenter")
                .setEnabled(true);

        GroupConfig groupConfig = new GroupConfig().setName("dev");
        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.getJoin()
                .getAwsConfig()
                .setEnabled(false);
        networkConfig.getJoin()
                .getMulticastConfig()
                .setEnabled(false);
        networkConfig.getJoin()
                .getTcpIpConfig()
                .addMember("localhost")
                .setEnabled(true);

        return new Config()
                .setInstanceName("hazelcast-instance")
                .addMapConfig(new MapConfig().setName("ticketsCache")
                        .setMaxSizeConfig(new MaxSizeConfig(200, FREE_HEAP_SIZE))
                        .setEvictionPolicy(LRU)
                        .setTimeToLiveSeconds(2000))
                .addMapConfig(new MapConfig().setName("allTickets")
                        .setMaxSizeConfig(new MaxSizeConfig(200, FREE_HEAP_SIZE))
                        .setEvictionPolicy(LRU)
                        .setTimeToLiveSeconds(2000))
                .setManagementCenterConfig(mcc)
                .setGroupConfig(groupConfig)
                .setNetworkConfig(networkConfig);
    }

    @Bean
    HazelcastInstance hazelcastInstance() {
        logger.info("Configuring Hazelcast");
        return newHazelcastInstance(hazelCastConfig());
    }

    @Bean
    CacheManager cacheManager() {
        logger.info("Starting HazelcastCacheManager");
        return new HazelcastCacheManager(hazelcastInstance());
    }


    @PreDestroy
    public void destroy() {
        logger.info("Closing Cache Manager");
        Hazelcast.shutdownAll();
    }

}
