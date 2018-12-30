package com.project.tchokonthe.service;


import com.project.tchokonthe.entities.Flight;
import com.project.tchokonthe.exceptions.FlightException;
import com.project.tchokonthe.repos.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.data.domain.Sort.DEFAULT_DIRECTION;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }


    public Flight createFlights(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight getFlightsById(Long flightId) {
        return flightRepository.findById(flightId).orElseThrow(() -> new FlightException("FlightReference " + flightId + " not founded"));
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll(sort());
    }

    private Sort sort() {
        return new Sort(DEFAULT_DIRECTION, newArrayList("departureHour", "arrivalHour"));
    }


}
