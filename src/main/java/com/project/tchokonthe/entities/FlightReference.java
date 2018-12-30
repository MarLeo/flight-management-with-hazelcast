package com.project.tchokonthe.entities;

import com.project.tchokonthe.entities.key.FlightId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;


@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flight_reference")
public class FlightReference implements Serializable {

    @EmbeddedId
    private FlightId flightId;


    @OneToMany(cascade = ALL)
    @JoinTable(name = "flights_reference",
            joinColumns =
                    {@JoinColumn(name = "departure"),
                            @JoinColumn(name = "arrival"),
                            @JoinColumn(name = "flight_day")},
            inverseJoinColumns = {@JoinColumn(name = "flight_id")})
    private Set<Flight> flights;

}
