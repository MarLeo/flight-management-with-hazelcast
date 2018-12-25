package com.project.tchokonthe.repos;

import com.project.tchokonthe.entities.Ticket;
import org.springframework.data.repository.CrudRepository;


public interface TicketBookingDao extends CrudRepository<Ticket, Integer> {
}
