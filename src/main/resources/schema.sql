CREATE TABLE IF NOT EXISTS `flight_reference` (
  `departure` varchar(255) NOT NULL,
  `arrival` varchar(255) NOT NULL,
  `flight_day` varchar(255) NOT NULL,
  PRIMARY KEY  (`departure`, `arrival`, `flight_day`)
);

CREATE TABLE IF NOT EXISTS `flight` (
  `flight_id` BIGINT NOT NULL auto_increment,
  `departure_hour` varchar(255) NOT NULL,
  `arrival_hour` varchar(255) NOT NULL,
  `seats` int(11) NOT NULL,
  PRIMARY KEY  (`flight_id`)
  );

CREATE TABLE IF NOT EXISTS `ticket` (
  `ticket_id` int(11) NOT NULL auto_increment,
  `departure` varchar(255) NOT NULL,
  `arrival` varchar(255) NOT NULL,
  `flight_day` varchar(255) NOT NULL,
  `flight_id` BIGINT NOT NULL,
  `passenger_name` varchar(255) NOT NULL,
  `email` varchar(255) default NULL,
  PRIMARY KEY  (`ticket_id`),
     CONSTRAINT `fk_flight_1`
    FOREIGN KEY (`departure`, `arrival`, `flight_day`)
       REFERENCES flight_reference(`departure`, `arrival`, `flight_day`),
       CONSTRAINT `fk_flights_1`
    FOREIGN KEY (`flight_id`)
       REFERENCES flight(`flight_id`)
);

CREATE TABLE IF NOT EXISTS `flights_reference`(
  `departure` varchar(255) NOT NULL,
  `arrival` varchar(255) NOT NULL,
  `flight_day` varchar(255) NOT NULL,
  `flight_id` BIGINT NOT NULL);

create sequence SEQ_FLIGHTS_ID INCREMENT BY 1 START WITH 1 MINVALUE 1;
create sequence SEQ_TICKET_ID INCREMENT BY 1 START WITH 1 MINVALUE 1;