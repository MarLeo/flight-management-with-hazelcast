package com.project.tchokonthe.entities.key;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Embeddable
public class FlightId implements Comparable<FlightId>, Serializable {

    @Column(name = "departure", nullable = false)
    private String departure;

    @Column(name = "arrival", nullable = false)
    private String arrival;

    /*@Temporal(DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")*/
    @Column(name = "flight_day", nullable = false)
    private String flightDay;

    @Override
    public int compareTo(FlightId flightId) {
        int dep = this.departure.compareTo(flightId.departure);
        int arr = this.arrival.compareTo(flightId.arrival);
        int day = this.flightDay.compareTo(flightId.flightDay);
        return day != 0 ? day : dep != 0 ? dep : arr;
//        return dep != 0 ? dep : arr != 0 ? arr : day;
    }
}
