package com.project.tchokonthe.entities;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "flight")
public class Flight implements Serializable {

    @Id
    @GeneratedValue(generator="SEQUENCE_GENERATOR_FLIGHT", strategy = SEQUENCE)
    @SequenceGenerator(name="SEQUENCE_GENERATOR_FLIGHT", sequenceName="SEQ_FLIGHTS_ID", allocationSize = 1)
    @Column(name = "flight_id", nullable = false)
    private Long flightId;

    @Column(name = "departure_hour", nullable = false)
    private String departureHour;

    @Column(name = "arrival_hour", nullable = false)
    private String arrivalHour;

    @Column(name = "seats", nullable = false)
    private Integer seats;
}
