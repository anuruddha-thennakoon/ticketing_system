package com.teamreact.app.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Time entity.
 */
public class TimeDTO implements Serializable {

    private Long id;

    @NotNull
    private String route;

    private String bus_no;

    private String from;

    private String to;

    private String departure;

    private String arrival;

    private String frequency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getBus_no() {
        return bus_no;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimeDTO timeDTO = (TimeDTO) o;
        if(timeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TimeDTO{" +
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
