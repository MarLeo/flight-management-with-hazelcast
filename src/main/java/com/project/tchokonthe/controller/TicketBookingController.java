package com.project.tchokonthe.controller;


import com.project.tchokonthe.entities.Ticket;
import com.project.tchokonthe.service.TicketBookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/api/flight")
public class TicketBookingController {

    private final TicketBookingService ticketBookingService;
    private final CacheManager cacheManager;

    private static final Logger logger = LoggerFactory.getLogger(TicketBookingController.class);

    @Autowired
    public TicketBookingController(TicketBookingService ticketBookingService,
                                   CacheManager cacheManager) {
        this.ticketBookingService = ticketBookingService;
        this.cacheManager = cacheManager;
    }

    @PostConstruct
    public void init() {
        logger.info("Using cache manager: " + cacheManager + " and cache names are: " + cacheManager.getCacheNames());
    }


    @GetMapping("/v1.0/all")
    public List<Ticket> getAllTickets() {
        logger.info("Get all Tickets");
        return ticketBookingService.getAllTickets();
    }

    @PostMapping("/v1.0/created")
    public Ticket createTicket(@RequestBody Ticket ticket) {
        logger.info("Creating a new ticket");
        return ticketBookingService.createTicket(ticket);
    }


    @GetMapping("/v1.0/{ticketId}")
    public Ticket getTicketById(@PathVariable("ticketId") Integer ticketId) {
        logger.info("Searching ticket " + ticketId);
        return ticketBookingService.getTicketById(ticketId);
    }

    @PutMapping("/v1.0/update")
    public Ticket updateTicket(@RequestBody Ticket ticket) {
        logger.info("Updating ticket " + ticket);
        return ticketBookingService.updateTicket(ticket);
    }

    @DeleteMapping("/v1.0/{ticketId}")
    public void deleteTicket(@PathVariable("ticketId") Integer ticketId) {
        logger.info("deleting ticket " + ticketId);
        ticketBookingService.deleteTicket(ticketId);
    }
}
