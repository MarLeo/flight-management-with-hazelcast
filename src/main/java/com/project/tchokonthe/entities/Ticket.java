package com.project.tchokonthe.entities;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

//@Cache(usage = READ_WRITE)
@Data
@Entity
@Table(name = "ticket")
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(generator = "SEQUENCE_GENERATOR", strategy = SEQUENCE)
    @SequenceGenerator(name = "SEQUENCE_GENERATOR", sequenceName = "SEQ_FLIGHTS_ID", allocationSize = 1)
    @Column(name = "ticket_id")
    private Integer ticketId;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @OneToOne(fetch = LAZY, targetEntity = FlightReference.class)
    @JoinColumns({
            @JoinColumn(name = "departure", referencedColumnName = "departure"),
            @JoinColumn(name = "arrival", referencedColumnName = "arrival"),
            @JoinColumn(name = "flight_day", referencedColumnName = "flight_day")})
    private FlightReference flightReference;


    @OneToOne(fetch = LAZY, targetEntity = Flight.class)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Column(name = "email")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", flags = CASE_INSENSITIVE)
    private String email;


}
