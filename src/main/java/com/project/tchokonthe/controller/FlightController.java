package com.project.tchokonthe.controller;


import com.project.tchokonthe.entities.Flight;
import com.project.tchokonthe.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }


    @PostMapping("/v1.0/create")
    public Flight create(@RequestBody Flight flight){
        return flightService.createFlights(flight);
    }

    @GetMapping("/v1.0/{flightId}")
    public Flight findById(@PathVariable("flightId") Long flightId) {
        return flightService.getFlightsById(flightId);
    }

    @GetMapping("/v1.0/all")
    public List<Flight> findAll() {
        return flightService.getAllFlights();
    }


}
