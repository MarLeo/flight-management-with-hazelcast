package com.project.tchokonthe.service;

import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HazelcastService {

    private final HazelcastInstance hazelcastInstance;

    @Autowired
    public HazelcastService(@Qualifier("flightManagementHzInstance") HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }


    public Map<String, CacheSimpleConfig> instances() {
        return hazelcastInstance.getConfig().getCacheConfigs();
    }

}
