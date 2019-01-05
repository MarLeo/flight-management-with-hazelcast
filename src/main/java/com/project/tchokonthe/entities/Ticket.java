package com.project.tchokonthe.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.validation.constraints.Pattern.Flag.CASE_INSENSITIVE;

//@Cache(usage = READ_WRITE)
@Data
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ticket")
@JsonIgnoreProperties(value = {"reference.flights"})
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(generator = "SEQUENCE_GENERATOR_TICKET", strategy = SEQUENCE)
    @SequenceGenerator(name = "SEQUENCE_GENERATOR_TICKET", sequenceName = "SEQ_TICKET_ID", allocationSize = 1)
    @Column(name = "ticket_id")
    private Integer ticketId;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @OneToOne(fetch = EAGER, targetEntity = FlightReference.class)
    /*@JoinTable(name = "flight_reference",
            joinColumns = {
                    @JoinColumn(name = "departure", referencedColumnName = "departure"),
                    @JoinColumn(name = "arrival", referencedColumnName = "arrival"),
                    @JoinColumn(name = "flight_day", referencedColumnName = "flight_day")})*/
    @JoinColumns({
            @JoinColumn(name = "departure", referencedColumnName = "departure"),
            @JoinColumn(name = "arrival", referencedColumnName = "arrival"),
            @JoinColumn(name = "flight_day", referencedColumnName = "flight_day")})
    private FlightReference reference;


    @OneToOne(fetch = LAZY, targetEntity = Flight.class)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Column(name = "email")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", flags = CASE_INSENSITIVE)
    private String email;

    public Ticket(String passengerName, FlightReference reference, Flight flight, @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", flags = CASE_INSENSITIVE) String email) {
        this.passengerName = passengerName;
        this.reference = reference;
        this.flight = flight;
        this.email = email;
    }
}
