package com.project.tchokonthe.controller;


import com.google.common.collect.Lists;
import com.hazelcast.core.IMap;
import com.hazelcast.monitor.LocalMapStats;
import com.project.tchokonthe.entities.Flight;
import com.project.tchokonthe.entities.FlightReference;
import com.project.tchokonthe.entities.Ticket;
import com.project.tchokonthe.entities.key.FlightId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hazelcast")
public class HazelcastController {

    private final CacheManager cacheManager;
    private final IMap<Long, Flight> flightMap;
    private final IMap<FlightId, FlightReference> flightReferenceMap;
    private final IMap<Integer, Ticket> ticketMap;


    @Autowired
    public HazelcastController(CacheManager cacheManager,
                               IMap<Long, Flight> flightMap,
                               IMap<FlightId, FlightReference> flightReferenceMap,
                               IMap<Integer, Ticket> ticketMap) {
        this.cacheManager = cacheManager;
        this.flightMap = flightMap;
        this.flightReferenceMap = flightReferenceMap;
        this.ticketMap = ticketMap;
    }



    @GetMapping("/v1.0/caches")
    public List<String> instances() {
        return Lists.newArrayList(cacheManager.getCacheNames()); /*hazelcastInstance.getConfig().getMapConfigs();*/
    }


    @GetMapping("/v1.0/flightsStats")
    public LocalMapStats flightsStats() {
        return  flightMap.getLocalMapStats();
    }
}
