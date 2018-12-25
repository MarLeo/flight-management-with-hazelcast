package com.project.tchokonthe.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.project.tchokonthe.entities.Ticket;
import com.project.tchokonthe.exceptions.TicketNotFoundException;
import com.project.tchokonthe.repos.TicketBookingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
public class TicketBookingService {

    private final TicketBookingDao ticketBookingDao;
    private final HazelcastInstance hazelcastInstance;

    private static final Logger logger = LoggerFactory.getLogger(TicketBookingService.class);

    IMap<Integer, Ticket> map;

    @Autowired
    public TicketBookingService(TicketBookingDao ticketBookingDao,
                                HazelcastInstance hazelcastInstance) {
        this.ticketBookingDao = ticketBookingDao;
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostConstruct
    public void init() {
        map = hazelcastInstance.getMap("ticketsCache");
        map.addIndex("ticketId", true);
        logger.info("tickets cache: " + map.size());
    }


    //    @CachePut(value = "ticketsCache")
    public Ticket createTicket(Ticket ticket) {
        Ticket tckt = ticketBookingDao.save(ticket);
        map.put(tckt.getTicketId(), tckt);
        return tckt;
    }

    //    @Cacheable(value = "ticketsCache", key = "#ticketId", unless = "#result==null")
    public Ticket getTicketById(Integer ticketId) {
        final Ticket tckt = map.get(ticketId);
        if (tckt != null) {
            logger.info("Ticket " + tckt.getTicketId() + " has been founded in HZ");
            return tckt;
        } else {
            final Optional<Ticket> ticket = ticketBookingDao.findById(ticketId);
            if (ticket.isPresent()) {
                final Ticket found = ticket.get();
                logger.info("Ticket " + found.getTicketId() + " has been founded in DB");
                if (map.putIfAbsent(found.getTicketId(), found)!=null) {
                    logger.info("The ticket " + found.getTicketId() + " has been inserted in HZ");
                }
                return found;
            }
            throw new TicketNotFoundException("Ticket with id " + ticketId + " not founded.");
        }


        /*return ticket.ifPresent(map.put(1, null))

                .orElseThrow(() -> new TicketNotFoundException("Ticket with id " + ticketId + " not founded."));*/
    }

    //    @Cacheable(value = "allTickets")
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>(map.values());
        if (tickets != null) {
            logger.info( tickets.size() + "Tickets has been founded in Hazelcast");
            return tickets;
        } else {
            logger.info("Tickets were not in Hazelcast but will be inserted from DB");
            final List<Ticket> ticketList = StreamSupport.stream(ticketBookingDao.findAll().spliterator(), false).collect(toList());
            ticketList.forEach(t -> map.putIfAbsent(t.getTicketId(), t));
            return ticketList;
        }
    }

    //    @CacheEvict(value = "ticketsCache", key = "#ticketId")
    public void deleteTicket(Integer ticketId) {
        if (map.containsKey(ticketId)) {
            logger.info("Ticket " + map.remove(ticketId).getTicketId() + " has been removed from HZ and DB");
            ticketBookingDao.deleteById(ticketId);
        } else {
            if (ticketBookingDao.findById(ticketId).isPresent()) {
            logger.info("Ticket " + ticketId + " was not present in HZ but will just be remove from DB");
            ticketBookingDao.deleteById(ticketId);
            } else {
                logger.info("Ticket " + ticketId + " does not existed.");
            }
        }
    }

    //    @CachePut(value = "ticketsCache")
    public Ticket updateTicket(Ticket ticket) {
        if (map.containsKey(ticket.getTicketId())) {
            Ticket old = map.put(ticket.getTicketId(), ticket);
            if (old != ticket) logger.info("The old ticket " + old + " has been updated to " + ticket);
            return ticket;
        } else {
            logger.info("Ticket " + ticket.getTicketId() + " was not in HZ it will be retrieve from Db and load into HZ");
            final Ticket save = ticketBookingDao.save(ticket);
            map.put(save.getTicketId(), save);
            return save;
        }
    }
}
