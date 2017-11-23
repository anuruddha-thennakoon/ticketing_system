package com.teamreact.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BusRoute.
 */
@Entity
@Table(name = "bus_route")
@Document(indexName = "busroute")
public class BusRoute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_number")
    private String routeNumber;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "route")
    @JsonIgnore
    private Set<Journey> journeys = new HashSet<>();

    @ManyToMany(mappedBy = "routes")
    @JsonIgnore
    private Set<TimeTable> timeTables = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public BusRoute routeNumber(String routeNumber) {
        this.routeNumber = routeNumber;
        return this;
    }

    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }

    public String getDescription() {
        return description;
    }

    public BusRoute description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Journey> getJourneys() {
        return journeys;
    }

    public BusRoute journeys(Set<Journey> journeys) {
        this.journeys = journeys;
        return this;
    }

    public BusRoute addJourney(Journey journey) {
        this.journeys.add(journey);
        journey.setRoute(this);
        return this;
    }

    public BusRoute removeJourney(Journey journey) {
        this.journeys.remove(journey);
        journey.setRoute(null);
        return this;
    }

    public void setJourneys(Set<Journey> journeys) {
        this.journeys = journeys;
    }

    public Set<TimeTable> getTimeTables() {
        return timeTables;
    }

    public BusRoute timeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
        return this;
    }

    public BusRoute addTimeTable(TimeTable timeTable) {
        this.timeTables.add(timeTable);
        timeTable.getRoutes().add(this);
        return this;
    }

    public BusRoute removeTimeTable(TimeTable timeTable) {
        this.timeTables.remove(timeTable);
        timeTable.getRoutes().remove(this);
        return this;
    }

    public void setTimeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
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
        BusRoute busRoute = (BusRoute) o;
        if (busRoute.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), busRoute.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BusRoute{" +
            "id=" + getId() +
            ", routeNumber='" + getRouteNumber() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
