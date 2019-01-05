package com.project.tchokonthe.config;


import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import com.project.tchokonthe.entities.Flight;
import com.project.tchokonthe.entities.FlightReference;
import com.project.tchokonthe.entities.Ticket;
import com.project.tchokonthe.entities.key.FlightId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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
        networkConfig.setPortCount(4);
        networkConfig.setPublicAddress("169.254.55.132:5701");
        networkConfig.setPublicAddress("169.254.55.132:5702");
        networkConfig.setPublicAddress("169.254.55.132:5703");
                /*.addMember("localhost")
                .addMember("169.254.55.132")
                .setEnabled(true);*/

        return new Config()
                .setInstanceName("hazelcast-instance")
                .addMapConfig(new MapConfig().setName("flightsBooking")
                        .setMaxSizeConfig(new MaxSizeConfig(200, FREE_HEAP_SIZE))
                        .setEvictionPolicy(LRU)
                        .setTimeToLiveSeconds(2000))
                /*.addMapConfig(new MapConfig()*//*.setName("allTickets")*//*
                        .setMaxSizeConfig(new MaxSizeConfig(200, FREE_HEAP_SIZE))
                        .setEvictionPolicy(LRU)
                        .setTimeToLiveSeconds(2000))*/
                .setManagementCenterConfig(mcc)
                .setGroupConfig(groupConfig)
                .setNetworkConfig(networkConfig);
    }

    @Bean
    @Qualifier("flightManagementHzInstance")
    HazelcastInstance hazelcastInstance() {
        logger.info("Configuring Hazelcast");
        return newHazelcastInstance(hazelCastConfig());
    }

    @Bean
    CacheManager cacheManager() {
        logger.info("Starting HazelcastCacheManager");
        return new HazelcastCacheManager(hazelcastInstance());
    }

    @Bean
    IMap<Integer, Ticket> ticketIMap() {
        logger.info("Instantiating tickets cache");
        return hazelcastInstance().getMap("ticketsCache");
    }


    @Bean
    IMap<FlightId, FlightReference> flightReferenceIMap() {
        logger.info("Instantiating flight reference cache");
        return hazelcastInstance().getMap("flightReferenceMap");
    }

    @Bean
    IMap<Long, Flight> flightsIMap() {
        logger.info("Instantiating flights cache");
        return hazelcastInstance().getMap("flightsMap");
    }


    @PreDestroy
    public void destroy() {
        logger.info("Closing Cache Manager");
        Hazelcast.shutdownAll();
    }

}
