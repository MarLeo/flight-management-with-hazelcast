-- INSERT INTO ticket(passenger_name, flight_departure, flight_arrival, flight_flight_day, flights_flight_id, email) VALUES('Sean','Chennai',' Mumbai','2017-08-23',1,'sean.s2017@yahoo.com');
-- INSERT INTO ticket(passenger_name,booking_date,source_station,dest_station,email) VALUES('Raj','2017-08-12','Chennai','Mumbai','raj.s2007@siffy.com');
-- INSERT INTO ticket(passenger_name,booking_date,source_station,dest_station,email) VALUES('Martin','2017-08-15','Delhi','Mumbai','martin.s2001@xyz.com');
-- INSERT INTO ticket(passenger_name,booking_date,source_station,dest_station,email) VALUES('John','2017-08-21','Chennai','Mumbai','john.s2011@yahoo.com');

-- INSERT INTO flightReference(departure, arrival, flight_day, seats) VALUES('Chennai','Mumbai','2017-08-23', 300);
-- INSERT INTO flightReference(departure, arrival, flight_day, seats) VALUES('Delhi','Mumbai','2017-08-12', 100);
-- INSERT INTO flightReference(departure, arrival, flight_day, seats) VALUES('Pune','Mumbai','2017-05-23', 100);


INSERT INTO flight(departure_hour, arrival_hour, seats) VALUES('10:00:00', '12:00:00', 100);


-- INSERT INTO flight_flights(departure, arrival, flight_day, flight_id) VALUES('Chennai','Mumbai','2017-08-23', 1);