package com.project.tchokonthe.service;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.project.tchokonthe.entities.Flight;
import com.project.tchokonthe.exceptions.FlightException;
import com.project.tchokonthe.repos.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.data.domain.Sort.DEFAULT_DIRECTION;

@Service
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);


    private final FlightRepository flightRepository;
    private final HazelcastInstance hazelcastInstance;
    private final IMap<Long, Flight> flightMap;


    @Autowired
    public FlightService(FlightRepository flightRepository,
                         @Qualifier("flightManagementHzInstance") HazelcastInstance hazelcastInstance,
                         IMap<Long, Flight> flightMap) {
        this.flightRepository = flightRepository;
        this.hazelcastInstance = hazelcastInstance;
        this.flightMap = flightMap;
    }

    @PostConstruct
    public void init() {
        flightMap.addIndex("flightId", true);
        flightRepository.findAll(sort()).forEach(f -> flightMap.putIfAbsent(f.getFlightId(), f));
        logger.info("Flights in the cache: " + flightMap.size());
    }


    public Flight createFlights(Flight flight) {
        final Flight save = flightRepository.save(flight);
        if (save != null) {
            Long flightId = save.getFlightId();
            flightMap.putIfAbsent(flightId, save);
            if (flightMap.get(flightId) != null) {
                logger.info("Flight " + flightId + " has been inserted in HZ");
            }
            logger.info("Flight " + flightId + " has been inserted in the DB");
        }
        return save;
    }

    public Flight getFlightsById(Long flightId) {

        final Flight flight = flightMap.get(flightId);
        if (flight != null) {
            logger.info("Flight " + flightId + " has been retrieved from HZ");
            return flight;
        } else {
            final Optional<Flight> found = flightRepository.findById(flightId);
            if (found.isPresent()) {
                final Flight flight1 = found.get();
                final Long id = flight1.getFlightId();
                flightMap.put(id, flight1);
                if (flightMap.get(id) != null) {
                    logger.info("Flight " + id + " has been inserted in HZ");
                }
                logger.info("Flight " + id + " has been retrieved from DB");
                return flight1;
            }
        }
        throw new FlightException("Flight " + flightId + " not founded");
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll(sort());
    }

    private Sort sort() {
        return new Sort(DEFAULT_DIRECTION, newArrayList("departureHour", "arrivalHour"));
    }


}
