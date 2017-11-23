package com.teamreact.app.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TimeTable.
 */
@Entity
@Table(name = "time_table")
@Document(indexName = "timetable")
public class TimeTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "starting_from")
    private String startingFrom;

    @Column(name = "ending_from")
    private String endingFrom;

    @Column(name = "departure")
    private String departure;

    @Column(name = "arrival")
    private String arrival;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "bus_no")
    private String busNo;

    @ManyToMany
    @NotNull
    @JoinTable(name = "time_table_route",
               joinColumns = @JoinColumn(name="time_tables_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="routes_id", referencedColumnName="id"))
    private Set<BusRoute> routes = new HashSet<>();

    @ManyToOne
    private TransportManager transportManager;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartingFrom() {
        return startingFrom;
    }

    public TimeTable startingFrom(String startingFrom) {
        this.startingFrom = startingFrom;
        return this;
    }

    public void setStartingFrom(String startingFrom) {
        this.startingFrom = startingFrom;
    }

    public String getEndingFrom() {
        return endingFrom;
    }

    public TimeTable endingFrom(String endingFrom) {
        this.endingFrom = endingFrom;
        return this;
    }

    public void setEndingFrom(String endingFrom) {
        this.endingFrom = endingFrom;
    }

    public String getDeparture() {
        return departure;
    }

    public TimeTable departure(String departure) {
        this.departure = departure;
        return this;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public TimeTable arrival(String arrival) {
        this.arrival = arrival;
        return this;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getFrequency() {
        return frequency;
    }

    public TimeTable frequency(String frequency) {
        this.frequency = frequency;
        return this;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getBusNo() {
        return busNo;
    }

    public TimeTable busNo(String busNo) {
        this.busNo = busNo;
        return this;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public Set<BusRoute> getRoutes() {
        return routes;
    }

    public TimeTable routes(Set<BusRoute> busRoutes) {
        this.routes = busRoutes;
        return this;
    }

    public TimeTable addRoute(BusRoute busRoute) {
        this.routes.add(busRoute);
        busRoute.getTimeTables().add(this);
        return this;
    }

    public TimeTable removeRoute(BusRoute busRoute) {
        this.routes.remove(busRoute);
        busRoute.getTimeTables().remove(this);
        return this;
    }

    public void setRoutes(Set<BusRoute> busRoutes) {
        this.routes = busRoutes;
    }

    public TransportManager getTransportManager() {
        return transportManager;
    }

    public TimeTable transportManager(TransportManager transportManager) {
        this.transportManager = transportManager;
        return this;
    }

    public void setTransportManager(TransportManager transportManager) {
        this.transportManager = transportManager;
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
        TimeTable timeTable = (TimeTable) o;
        if (timeTable.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timeTable.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TimeTable{" +
            "id=" + getId() +
            ", startingFrom='" + getStartingFrom() + "'" +
            ", endingFrom='" + getEndingFrom() + "'" +
            ", departure='" + getDeparture() + "'" +
            ", arrival='" + getArrival() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", busNo='" + getBusNo() + "'" +
            "}";
    }
}
