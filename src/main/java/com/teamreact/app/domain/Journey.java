package com.teamreact.app.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Journey.
 */
@Entity
@Table(name = "journey")
@Document(indexName = "journey")
public class Journey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "journey_date")
    private String journeyDate;

    @Column(name = "starting_point")
    private String startingPoint;

    @Column(name = "destination")
    private String destination;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "bus_number")
    private String busNumber;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Payment payment;

    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private SmartCard smartCard;

    @ManyToOne(optional = false)
    @NotNull
    private BusRoute route;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public Journey journeyDate(String journeyDate) {
        this.journeyDate = journeyDate;
        return this;
    }

    public void setJourneyDate(String journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public Journey startingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
        return this;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getDestination() {
        return destination;
    }

    public Journey destination(String destination) {
        this.destination = destination;
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public Journey departureTime(String departureTime) {
        this.departureTime = departureTime;
        return this;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public Journey busNumber(String busNumber) {
        this.busNumber = busNumber;
        return this;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public Payment getPayment() {
        return payment;
    }

    public Journey payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Journey passenger(Passenger passenger) {
        this.passenger = passenger;
        return this;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public SmartCard getSmartCard() {
        return smartCard;
    }

    public Journey smartCard(SmartCard smartCard) {
        this.smartCard = smartCard;
        return this;
    }

    public void setSmartCard(SmartCard smartCard) {
        this.smartCard = smartCard;
    }

    public BusRoute getRoute() {
        return route;
    }

    public Journey route(BusRoute busRoute) {
        this.route = busRoute;
        return this;
    }

    public void setRoute(BusRoute busRoute) {
        this.route = busRoute;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Journey journey = (Journey) o;
        if (journey.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), journey.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Journey{" +
            "id=" + getId() +
            ", journeyDate='" + getJourneyDate() + "'" +
            ", startingPoint='" + getStartingPoint() + "'" +
            ", destination='" + getDestination() + "'" +
            ", departureTime='" + getDepartureTime() + "'" +
            ", busNumber='" + getBusNumber() + "'" +
            "}";
    }
}
