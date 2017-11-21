package com.teamreact.app.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Time.
 */
@Entity
@Table(name = "time")
@Document(indexName = "time")
public class Time implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "route", nullable = false)
    private String route;

    @Column(name = "bus_no")
    private String bus_no;

    @Column(name = "jhi_from")
    private String from;

    @Column(name = "jhi_to")
    private String to;

    @Column(name = "departure")
    private String departure;

    @Column(name = "arrival")
    private String arrival;

    @Column(name = "frequency")
    private String frequency;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public Time route(String route) {
        this.route = route;
        return this;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getBus_no() {
        return bus_no;
    }

    public Time bus_no(String bus_no) {
        this.bus_no = bus_no;
        return this;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }

    public String getFrom() {
        return from;
    }

    public Time from(String from) {
        this.from = from;
        return this;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public Time to(String to) {
        this.to = to;
        return this;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDeparture() {
        return departure;
    }

    public Time departure(String departure) {
        this.departure = departure;
        return this;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public Time arrival(String arrival) {
        this.arrival = arrival;
        return this;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getFrequency() {
        return frequency;
    }

    public Time frequency(String frequency) {
        this.frequency = frequency;
        return this;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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
        Time time = (Time) o;
        if (time.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), time.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Time{" +
            "id=" + getId() +
            ", route='" + getRoute() + "'" +
            ", bus_no='" + getBus_no() + "'" +
            ", from='" + getFrom() + "'" +
            ", to='" + getTo() + "'" +
            ", departure='" + getDeparture() + "'" +
            ", arrival='" + getArrival() + "'" +
            ", frequency='" + getFrequency() + "'" +
            "}";
    }
}
