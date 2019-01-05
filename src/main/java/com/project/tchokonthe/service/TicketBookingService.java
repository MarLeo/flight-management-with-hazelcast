package com.project.tchokonthe.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.project.tchokonthe.entities.Ticket;
import com.project.tchokonthe.exceptions.TicketNotFoundException;
import com.project.tchokonthe.repos.TicketBookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Comparator.comparing;
import static org.springframework.data.domain.Sort.DEFAULT_DIRECTION;

@Service
public class TicketBookingService {

    private static final Logger logger = LoggerFactory.getLogger(TicketBookingService.class);

    private final TicketBookingRepository ticketBookingRepository;
    private final HazelcastInstance hazelcastInstance;


    private IMap<Integer, Ticket> map;

    @Autowired
    public TicketBookingService(TicketBookingRepository ticketBookingRepository,
                                @Qualifier("flightManagementHzInstance") HazelcastInstance hazelcastInstance,
                                IMap<Integer, Ticket> map) {
        this.ticketBookingRepository = ticketBookingRepository;
        this.hazelcastInstance = hazelcastInstance;
        this.map = map;
    }

    @PostConstruct
    public void init() {
//        map = hazelcastInstance.getMap("ticketsCache");
        map.addIndex("ticketId", true);
        ticketBookingRepository.findAll(sortAsc())
                .forEach(t -> map.putIfAbsent(t.getTicketId(), t));
        logger.info("tickets cache: " + map.size());
    }


    //    @CachePut(value = "ticketsCache")
    public Ticket createTicket(Ticket ticket) {
        Ticket tckt = ticketBookingRepository.save(ticket);
        if (tckt != null) {
            logger.info("Ticket " + tckt.getTicketId() + " has been inserted in the DB");
        }
        map.putIfAbsent(ticket.getTicketId(), tckt);
        if (map.get(ticket.getTicketId()) != null) {
            logger.info("Ticket " + ticket.getTicketId() + " has been inserted in HZ");
        }
        return tckt;
    }

    //    @Cacheable(value = "ticketsCache", key = "#ticketId", unless = "#result==null")
    public Ticket getTicketById(Integer ticketId) {
        final Ticket tckt = map.get(ticketId);
        if (tckt != null) {
            logger.info("Ticket " + tckt.getTicketId() + " has been retrieved from HZ");
            return tckt;
        } else {
            final Optional<Ticket> ticket = ticketBookingRepository.findById(ticketId);
            if (ticket.isPresent()) {
                final Ticket found = ticket.get();
                logger.info("Ticket " + found.getTicketId() + " has been retrieved from DB");
                map.putIfAbsent(found.getTicketId(), found);
                if (map.get(found.getTicketId()) != null) {
                    logger.info("The ticket " + found.getTicketId() + " has been inserted in HZ");
                }
                return found;
            }
            throw new TicketNotFoundException("Ticket with id " + ticketId + " not founded.");
        }
    }

    //    @Cacheable(value = "allTickets")
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = newArrayList(map.values());
        final int size = tickets.size();
        if (size != 0) { // TODO: should refactor this part because if we just have one records it will load them from HZ and that what we want
            logger.info(size + " Tickets has been retrieved from Hazelcast");
            tickets.sort(comparing(Ticket::getTicketId));
            return tickets;
        } else {
            logger.info("Tickets were not in Hazelcast but will be inserted from DB");
            final List<Ticket> ticketList = new ArrayList<>(ticketBookingRepository.findAll(sortAsc()));
            ticketList.forEach(t -> map.putIfAbsent(t.getTicketId(), t));
            return ticketList;
        }
    }

    //    @CacheEvict(value = "ticketsCache", key = "#ticketId")
    public void deleteTicket(Integer ticketId) {
        if (map.containsKey(ticketId)) {
            logger.info("Ticket " + map.remove(ticketId).getTicketId() + " has been removed from HZ and DB");
            ticketBookingRepository.deleteById(ticketId);
        } else {
            if (ticketBookingRepository.findById(ticketId).isPresent()) {
                logger.info("Ticket " + ticketId + " was not present in HZ but will just be remove from DB");
                ticketBookingRepository.deleteById(ticketId);
            } else {
                logger.info("Ticket " + ticketId + " does not existed.");
            }
        }
    }

    //    @CachePut(value = "ticketsCache")
    public Ticket updateTicket(Ticket ticket) {
        if (map.containsKey(ticket.getTicketId())) {
            // TODO : the update should also be saved in DB
            Ticket old = map.put(ticket.getTicketId(), ticket);
            if (old != ticket) logger.info("The old ticket " + old + " has been updated to " + ticket);
            return ticket;
        } else {
            logger.info("Ticket " + ticket.getTicketId() + " was not in HZ it will be retrieve from Db and load into HZ");
            final Ticket save = ticketBookingRepository.save(ticket);
            map.put(save.getTicketId(), save);
            return save;
        }
    }


    private Sort sortAsc() {
        return new Sort(DEFAULT_DIRECTION, "ticketId");
    }
}
