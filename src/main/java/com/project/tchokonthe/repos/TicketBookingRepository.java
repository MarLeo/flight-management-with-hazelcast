package com.project.tchokonthe.repos;

import com.project.tchokonthe.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketBookingRepository extends JpaRepository<Ticket, Integer> {

}
