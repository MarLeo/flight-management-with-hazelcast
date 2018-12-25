package com.project.tchokonthe.entities;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.TemporalType.TIMESTAMP;
import static lombok.AccessLevel.PRIVATE;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Cache(usage = READ_WRITE)
@Entity
@Table(name = "ticket")
@Data
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy= IDENTITY)
    @Column(name="ticket_id")
    private Integer ticketId;

    @Column(name="passenger_name",nullable=false)
    private String passengerName;

    @Column(name="booking_date",nullable=false)
    @Temporal(DATE)
    private Date bookingDate;

    @Column(name="source_station",nullable=false)
    private String sourceStation;

    @Column(name="dest_station",nullable=false)
    private String destStation;

    @Column(name="email")
    private String email;


}
