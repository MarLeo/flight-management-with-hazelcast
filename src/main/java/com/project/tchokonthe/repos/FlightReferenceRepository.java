package com.project.tchokonthe.repos;

import com.project.tchokonthe.entities.FlightReference;
import com.project.tchokonthe.entities.key.FlightId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightReferenceRepository extends JpaRepository<FlightReference, FlightId> {
}
