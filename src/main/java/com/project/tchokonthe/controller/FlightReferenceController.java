package com.project.tchokonthe.controller;


import com.project.tchokonthe.entities.FlightReference;
import com.project.tchokonthe.entities.key.FlightId;
import com.project.tchokonthe.service.FlightReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight")
public class FlightReferenceController {


    private final FlightReferenceService flightReferenceService;

    @Autowired
    public FlightReferenceController(FlightReferenceService flightReferenceService) {
        this.flightReferenceService = flightReferenceService;
    }


    @PostMapping("/v1.0/create")
    public FlightReference createFlight(@RequestBody FlightReference flightReference){
        return flightReferenceService.create(flightReference);
    }

    @GetMapping("/v1.0/find")
    public FlightReference getFlightById(@RequestBody FlightId flightId) {
        return flightReferenceService.getFlightById(flightId);
    }

    /*@GetMapping("/v1.0/{departure}/{arrival}/{flightDay}")
    public FlightReference getFlightById(@PathVariable("departure") String departure,
                                @PathVariable("arrival") String arrival,
                                @PathVariable("flightDay") Date flightDay) throws ParseException {
        return flightService.getFlightById(departure, arrival, flightDay);
    }*/


    @PutMapping("/v1.0/update")
    public FlightReference updateFlight(@RequestBody FlightReference flightReference) {
        return flightReferenceService.updateFlight(flightReference);
    }

    @GetMapping("/v1.0/all")
    public List<FlightReference> getAllFlights() {
        return flightReferenceService.getAllFlights();
    }




}
