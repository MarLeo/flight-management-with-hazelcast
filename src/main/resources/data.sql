-- INSERT INTO ticket(passenger_name,booking_date,source_station,dest_station,email) VALUES('Raj','2017-08-12','Chennai','Mumbai','raj.s2007@siffy.com');
-- INSERT INTO ticket(passenger_name,booking_date,source_station,dest_station,email) VALUES('Martin','2017-08-15','Delhi','Mumbai','martin.s2001@xyz.com');
-- INSERT INTO ticket(passenger_name,booking_date,source_station,dest_station,email) VALUES('John','2017-08-21','Chennai','Mumbai','john.s2011@yahoo.com');

INSERT INTO flight_reference(departure, arrival, flight_day) VALUES('Chennai','Mumbai','2017-08-23');
INSERT INTO flight_reference(departure, arrival, flight_day) VALUES('Delhi','Mumbai','2017-08-12');
INSERT INTO flight_reference(departure, arrival, flight_day) VALUES('Pune','Mumbai','2017-05-23');


INSERT INTO flight(departure_hour, arrival_hour, seats) VALUES('10:00:00', '12:00:00', 100);
INSERT INTO flight(departure_hour, arrival_hour, seats) VALUES('08:00:00', '14:00:00', 200);
INSERT INTO flight(departure_hour, arrival_hour, seats) VALUES('07:00:00', '16:00:00', 500);


INSERT INTO flights_reference(departure, arrival, flight_day, flight_id) VALUES('Chennai','Mumbai','2017-08-23', 1);


-- INSERT INTO ticket(passenger_name, departure, arrival, flight_day, flight_id, email) VALUES('Sean','Chennai',' Mumbai','2017-08-23',1,'sean.s2017@yahoo.com');
