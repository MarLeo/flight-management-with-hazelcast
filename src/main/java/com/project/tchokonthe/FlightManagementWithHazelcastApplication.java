package com.project.tchokonthe;

import com.project.tchokonthe.entities.Flight;
import com.project.tchokonthe.entities.FlightReference;
import com.project.tchokonthe.entities.Ticket;
import com.project.tchokonthe.entities.key.FlightId;
import com.project.tchokonthe.service.FlightReferenceService;
import com.project.tchokonthe.service.TicketBookingService;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;
import static javafx.stage.Modality.NONE;
import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
//@EnableCaching
public class FlightManagementWithHazelcastApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(FlightManagementWithHazelcastApplication.class);


    private TableView<Flight> flightTableView = new TableView<>();
    private TableView<FlightId> flightReferenceTableView = new TableView<>();
    private TableView<Ticket> ticketTableView = new TableView<>();
    private final HBox hb = new HBox();

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = run(FlightManagementWithHazelcastApplication.class, args);

//        System.out.println("Beans :" + Arrays.asList(context.getBeanDefinitionNames()));

        launch(args);
    }


    @Override
    public void start(Stage stage) {

        final String DATE_FORMAT = "yyyy-MM-dd";

        FlightReferenceService service = context.getBean(FlightReferenceService.class);
        TicketBookingService ticketBookingService = context.getBean(TicketBookingService.class);

        Scene scene = new Scene(new Group());
        stage.setTitle("Flights View Sample");
        stage.setWidth(500);
        stage.setHeight(500);

        final Label label = new Label("Flights Book");
        label.setFont(new Font("Arial", 20));

        flightTableView.setEditable(true);
        flightReferenceTableView.setEditable(false);
        ticketTableView.setEditable(true);

        TableColumn<FlightId, String> departure = new TableColumn<>("departure");
        TableColumn<FlightId, String> arrival = new TableColumn<>("arrival");
        TableColumn<FlightId, String> flightDay = new TableColumn<>("flightDay");

        TableColumn<Flight, Long> flightId = new TableColumn<>("flightId");
        TableColumn<Flight, String> dep = new TableColumn<>("departureHour");
        TableColumn<Flight, String> arr = new TableColumn<>("arrivalHour");
        TableColumn<Flight, Integer> seats = new TableColumn<>("seats");


//        TableColumn<Ticket, Long> ticketId = new TableColumn<>("ticketId");
        TableColumn<Ticket, String> passengerName = new TableColumn<>("passengerName");
        TableColumn<Ticket, String> flightDep = new TableColumn<>("departure");
        TableColumn<Ticket, String> flightArr = new TableColumn<>("arrival");
        TableColumn<Ticket, String> flightTicketDay = new TableColumn<>("flightDay");
        TableColumn<Ticket, String> flightDepHour = new TableColumn<>("departureHour");
        TableColumn<Ticket, String> flightArrivHour = new TableColumn<>("arrivalHour");
        TableColumn<Ticket, String> email = new TableColumn<>("email");


        final ObservableList<TableColumn<FlightId, ?>> flightReferenceTableViewColumns = flightReferenceTableView.getColumns();
        flightReferenceTableViewColumns.addAll(departure, arrival, flightDay);


        final ObservableList<TableColumn<Flight, ?>> tableColumns = flightTableView.getColumns();
        tableColumns.addAll(flightId, dep, arr, seats);

        final ObservableList<TableColumn<Ticket, ?>> ticketTableViewColumns = ticketTableView.getColumns();
        ticketTableViewColumns.addAll(passengerName, flightDep, flightArr, flightTicketDay, flightDepHour, flightArrivHour, email);

//        ticketId.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        passengerName.setCellValueFactory(new PropertyValueFactory<>("passengerName"));
        flightDep.setCellValueFactory(new PropertyValueFactory<>("departure"));
        flightArr.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        flightTicketDay.setCellValueFactory(new PropertyValueFactory<>("flightDay"));
        flightDepHour.setCellValueFactory(new PropertyValueFactory<>("departureHour"));
        flightArrivHour.setCellValueFactory(new PropertyValueFactory<>("arrivalHour"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));


        // Defines how to fill data for each cell.
        // Get value from property of Flight...
        setFlightCellValues(flightId, dep, arr, seats);

//        flightTableView.setItems(flights());


        departure.setCellValueFactory(new PropertyValueFactory<>("departure"));
        arrival.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        flightDay.setCellValueFactory(new PropertyValueFactory<>("flightDay"));


        final TextField departureField = new TextField();
        departureField.setPromptText("departure");
        departureField.setMaxWidth(departure.getPrefWidth());

        final TextField arrivalField = new TextField();
        arrivalField.setPromptText("arrival");
        arrivalField.setMaxWidth(arrival.getPrefWidth());

        final DatePicker datePicker = new DatePicker(LocalDate.now());
       /* final TextField flightDayField = new TextField(datePicker);
        flightDayField.setPromptText("flightDay");
        flightDayField.setMaxWidth(flightDay.getPrefWidth());*/


        final Button searchButton = new Button("Search");

        searchButton.setOnAction(event -> {
            FlightId id = new FlightId(departureField.getText(),
                    arrivalField.getText(),
                    datePicker.getValue().format(ofPattern(DATE_FORMAT)));
            final FlightReference flightReference = flightReference(service, id);
            flightReferenceTableView.setItems(observableArrayList(flightReference.getFlightId()));
        });


        flightReferenceTableView.setRowFactory(f -> {
            TableRow<FlightId> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 2 && (!row.isEmpty())) {
                    Label secondLabel = new Label("I'm a Label on new Window");

                    StackPane secondaryLayout = new StackPane();
                    secondaryLayout.getChildren().addAll(secondLabel, flightTableView);

                    Scene secondScene = new Scene(secondaryLayout, 230, 100);

                    FlightId id = row.getItem();
                    final Set<Flight> flights = service.getFlightById(id).getFlights();

                    flightTableView.setItems(observableArrayList(flights));

                    // New window (Stage)
                    Stage newWindow = new Stage();
                    newWindow.setTitle("Flights List");
                    newWindow.setScene(secondScene);

                    // Set position of second window, related to primary window.
                    newWindow.setX(stage.getX() + 200);
                    newWindow.setY(stage.getY() + 100);

                    newWindow.show();
                }
            });
            return row;
        });


        flightTableView.setRowFactory(f -> {
            TableRow<Flight> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Flight flight = row.getItem();
                    logger.info("double click on : " + flight);
                    final Optional<FlightId> first = flightReferenceTableView.getItems().stream().findFirst();
                   /* logger.info("flight reference:");
                    first.ifPresent(System.out::println);*/
                    if (first.isPresent()) {
                        final FlightId id = first.get();
                        Dialog<TicketDialog> dialog = new Dialog<>();
                        dialog.setTitle("Ticket Booking");
                        dialog.setHeaderText("Book your Flight");
                        DialogPane dialogPane = dialog.getDialogPane();
                        dialogPane.getButtonTypes().addAll(CANCEL, OK);

                        TextField passengerNameField = new TextField("passengerName");
                        TextField deparField = new TextField(id.getDeparture());
                        TextField arrivField = new TextField(id.getArrival());
                        TextField flight_day_field = new TextField(id.getFlightDay());
                        TextField depHourField = new TextField(flight.getDepartureHour());
                        TextField arrvHourField = new TextField(flight.getArrivalHour());
                        TextField emailField = new TextField("email");

                        dialogPane.setContent(new VBox(8,
                                passengerNameField,
                                deparField,
                                arrivField,
                                flight_day_field,
                                depHourField,
                                arrvHourField,
                                emailField));

                        dialog.setResultConverter((button) -> {
                            if (button == OK) {
                                FlightReference reference = flightReference(service, id);
                                Ticket ticket = new Ticket(passengerNameField.getText(), reference, flight, emailField.getText());
                                ticketBookingService(ticketBookingService, ticket);
                                return new TicketDialog(passengerNameField.getText(),
                                        deparField.getText(),
                                        arrivField.getText(),
                                        flight_day_field.getText(),
                                        depHourField.getText(),
                                        arrvHourField.getText(),
                                        emailField.getText());
                            }
                            return null;
                        });

                        Optional<TicketDialog> optionalResult = dialog.showAndWait();
                        optionalResult.ifPresent(System.out::println);
                    }
                }
            });
            return row;
        });


        hb.getChildren().addAll(departureField, arrivalField, datePicker, searchButton);
        hb.setSpacing(2);


        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, hb, flightReferenceTableView, flightTableView);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    @Data
    @AllArgsConstructor
    @ToString
    private static class TicketDialog {
        String passengerName;
        String departure;
        String arrival;
        String flightDay;
        String departureHour;
        String arrivalHour;
        String email;

    }

    private void setFlightCellValues(TableColumn<Flight, Long> flightId, TableColumn<Flight, String> departure, TableColumn<Flight, String> arrival, TableColumn<Flight, Integer> seats) {
        flightId.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        departure.setCellValueFactory(new PropertyValueFactory<>("departureHour"));
        arrival.setCellValueFactory(new PropertyValueFactory<>("arrivalHour"));
        seats.setCellValueFactory(new PropertyValueFactory<>("seats"));
    }

    private static FlightReference flightReference(FlightReferenceService service,
                                                   FlightId flightId) {
        return service.getFlightById(flightId);
    }

    private static Ticket ticketBookingService(TicketBookingService service, Ticket ticket) {
        return service.createTicket(ticket);
    }

    private static ObservableList<FlightId> flights(FlightReferenceService service) {
        final List<FlightId> flightIds = service
                .getAllFlights()
                .stream()
                .map(FlightReference::getFlightId)
                .sorted(comparing(FlightId::getFlightDay).thenComparing(FlightId::getDeparture).thenComparing(FlightId::getArrival))
                .collect(toList());
        return observableArrayList(flightIds);
    }

    private static ObservableList<FlightId> flight(FlightReferenceService service,
                                                   FlightId flightId) {

        return observableArrayList(flightReference(service, flightId).getFlightId());
    }

    private static ObservableList<Flight> flightsById(FlightReferenceService service,
                                                      FlightId flightId) {

        return observableArrayList(flightReference(service, flightId).getFlights());
    }

    private static ObservableList<Ticket> ticket(FlightId flightId, FlightReferenceService service, Flight flight) {
        FlightReference reference = service.getFlightById(flightId);
        Ticket ticket = new Ticket();
        ticket.setPassengerName("");
        ticket.setEmail("");
        ticket.setReference(reference);
        ticket.setFlight(flight);
        return observableArrayList(ticket);
    }

    private void showFlightDialog(Stage parent,
                                  final TableView<Flight> table,
                                  double y,
                                  final Set<Flight> flights) {

        Scene scene = new Scene(new Group());

        // initialize the dialog.
        final Stage dialog = new Stage();
        dialog.setTitle("Flights");
//        dialog.initOwner(parent);
        dialog.initModality(NONE);
//        dialog.initStyle(StageStyle.UTILITY);
        dialog.setX(parent.getX() + parent.getWidth());
        dialog.setY(y);

        final Label label = new Label("Flights Book");
        label.setFont(new Font("Arial", 20));

        table.setItems(observableArrayList(flights));

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        parent.setScene(scene);
        parent.show();
    }


    private void showTicketDialog(Stage parent, final TableView<Ticket> table, double y) {

        Scene scene = new Scene(new Group());

        // initialize the dialog.
        final Stage dialog = new Stage();
        dialog.setTitle("New Ticket");
        dialog.initOwner(parent);
        dialog.initModality(NONE);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setX(parent.getX() + parent.getWidth());
        dialog.setY(y);

        final Label label = new Label("Flights Book");
        label.setFont(new Font("Arial", 20));

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        parent.setScene(scene);
        parent.show();
    }



   /* private void showTicketDialog(Stage parent, final TableView<Person> table, double y) {
        // initialize the dialog.
        final Stage dialog = new Stage();
        dialog.setTitle("New Person");
        dialog.initOwner(parent);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setX(parent.getX() + parent.getWidth());
        dialog.setY(y);

        // create a grid for the data entry.
        GridPane grid = new GridPane();
        final TextField firstNameField = new TextField();
        final TextField lastNameField = new TextField();
        grid.addRow(0, new Label("First Name"), firstNameField);
        grid.addRow(1, new Label("Last Name"), lastNameField);
        grid.setHgap(10);
        grid.setVgap(10);
        GridPane.setHgrow(firstNameField, Priority.ALWAYS);
        GridPane.setHgrow(lastNameField, Priority.ALWAYS);

        // create action buttons for the dialog.
        Button ok = new Button("OK");
        ok.setDefaultButton(true);
        Button cancel = new Button("Cancel");
        cancel.setCancelButton(true);

        // only enable the ok button when there has been some text entered.
        ok.disableProperty().bind(firstNameField.textProperty().isEqualTo("").or(lastNameField.textProperty().isEqualTo("")));

        // add action handlers for the dialog buttons.
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                int nextIndex = table.getSelectionModel().getSelectedIndex() + 1;
                table.getItems().add(nextIndex, new Person(firstNameField.getText(), lastNameField.getText()));
                table.getSelectionModel().select(nextIndex);
                dialog.close();
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                dialog.close();
            }
        });

        // layout the dialog.
        HBox buttons = HBoxBuilder.create().spacing(10).children(ok, cancel).alignment(Pos.CENTER_RIGHT).build();
        VBox layout = new VBox(10);
        layout.getChildren().addAll(grid, buttons);
        layout.setPadding(new Insets(5));
        dialog.setScene(new Scene(layout));
        dialog.show();
    }*/

}

