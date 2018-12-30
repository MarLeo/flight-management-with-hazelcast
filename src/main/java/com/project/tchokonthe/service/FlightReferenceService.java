package com.project.tchokonthe.service;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.project.tchokonthe.entities.Flight;
import com.project.tchokonthe.entities.FlightReference;
import com.project.tchokonthe.entities.key.FlightId;
import com.project.tchokonthe.exceptions.FlightException;
import com.project.tchokonthe.repos.FlightReferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.data.domain.Sort.DEFAULT_DIRECTION;

@Service
public class FlightReferenceService {

    private static final Logger logger = LoggerFactory.getLogger(FlightReferenceService.class);

    private final FlightReferenceRepository flightReferenceRepository;
    private final HazelcastInstance hazelcastInstance;

    private final IMap<FlightId, FlightReference> flightReferenceMap;

    @Autowired
    public FlightReferenceService(FlightReferenceRepository flightReferenceRepository,
                                  @Qualifier("flightManagementHzInstance") HazelcastInstance hazelcastInstance,
                                  IMap<FlightId, FlightReference> flightReferenceMap) {
        this.flightReferenceRepository = flightReferenceRepository;
        this.hazelcastInstance = hazelcastInstance;
        this.flightReferenceMap = flightReferenceMap;
    }

    @PostConstruct
    public void init() {
        flightReferenceMap.addIndex("flightId", true);
        flightReferenceRepository.findAll(sortByDayAsc())
                .forEach(flight -> flightReferenceMap.put(flight.getFlightId(), flight));
        logger.info("flights in cache: " + flightReferenceMap.size());
    }

    public FlightReference create(FlightReference flightReference) {

        final FlightReference created = flightReferenceRepository.save(flightReference);
        if (created != null) {
            final FlightId flightId = created.getFlightId();
            logger.info("Flight " + flightId + " has been inserted in the DB");
            flightReferenceMap.put(flightId, created);
            if (flightReferenceMap.get(flightId) != null) {
                logger.info("Flight " + flightId + " has been inserted in HZ");
            }
            return created;
        }
        throw new FlightException("Flight " + flightReference.getFlightId() + " has not been created");
    }

    public FlightReference getFlightById(FlightId flightId) {
        final FlightReference flightReference = flightReferenceMap.get(flightId);
        if (flightReference != null) {
            logger.info("Flight " + flightId + " has been retrieved from HZ");
            return flightReference;
        } else {
            final Optional<FlightReference> reference = flightReferenceRepository.findById(flightId);
            if (reference.isPresent()) {
                FlightReference found = reference.get();
                flightReferenceMap.put(found.getFlightId(), found);
                if (flightReferenceMap.get(found.getFlightId()) != null) {
                    logger.info("Flight " + flightId + " has been inserted in HZ");
                }
                logger.info("Flight " + flightId + " has been retrieved from DB");
                return found;
            }
        }
        throw new FlightException("FlightReference " + flightId + " was not found");
    }

    public List<FlightReference> getAllFlights() {
        final List<FlightReference> values = newArrayList(flightReferenceMap.values());
        final int size = values.size();
        if (size != 0) {
            logger.info(size + " flights has been founded in HZ");
            return values;
        } else {
            logger.info("Flights will be inserted from DB");
            final List<FlightReference> all = flightReferenceRepository.findAll(sortByDayAsc());
            all.forEach(flight -> flightReferenceMap.put(flight.getFlightId(), flight));
            return all;
        }
    }

    @Transactional
    public FlightReference updateFlight(FlightReference flightReference) {
//        updateInDB(flightReference);
        flightReferenceRepository.save(updateInDB(flightReference));
        final FlightId flightId = flightReference.getFlightId();
        if (flightReferenceMap.containsKey(flightId)) {
            final Set<Flight> flights = flightReferenceMap.get(flightId).getFlights();
            flights.addAll(flightReference.getFlights());
            flightReference.setFlights(flights);
            FlightReference old = flightReferenceMap.put(flightId, flightReference);
            if (old != flightReferenceMap.get(flightId)) {
                logger.info("Old flight " + old + " has been updated to " + flightReference + " in HZ");
            }
//            flightReferenceRepository.save(flightReferenceMap.put(flightId, flightReference));
        }
        return flightReference;

        /*else {
            logger.info("Flight " + flightReference.getFlightId() + " was not in HZ it will be retrieve from Db and load into HZ");
            final FlightReference save = updateInDB(flightReference);
            flightReferenceMap.put(flightReference.getFlightId(), flightReference);
            return save;
        }*/
    }

    private FlightReference updateInDB(FlightReference flightReference) {
        final FlightReference flights = getFlightById(flightReference.getFlightId());
        final Set<Flight> flightSet = flights.getFlights();
        flightSet.addAll(flightReference.getFlights());
        return flights;
    }

    private Sort sortByDayAsc() {
        return new Sort(DEFAULT_DIRECTION, "flightId.departure", "flightId.arrival", "flightId.flightDay");
    }


}
