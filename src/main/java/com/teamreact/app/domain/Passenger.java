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
 * A Passenger.
 */
@Entity
@Table(name = "passenger")
@Document(indexName = "passenger")
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nic")
    private String nic;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "passenger")
    @JsonIgnore
    private Set<Journey> journeys = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNic() {
        return nic;
    }

    public Passenger nic(String nic) {
        this.nic = nic;
        return this;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getName() {
        return name;
    }

    public Passenger name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Journey> getJourneys() {
        return journeys;
    }

    public Passenger journeys(Set<Journey> journeys) {
        this.journeys = journeys;
        return this;
    }

    public Passenger addJourney(Journey journey) {
        this.journeys.add(journey);
        journey.setPassenger(this);
        return this;
    }

    public Passenger removeJourney(Journey journey) {
        this.journeys.remove(journey);
        journey.setPassenger(null);
        return this;
    }

    public void setJourneys(Set<Journey> journeys) {
        this.journeys = journeys;
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
        Passenger passenger = (Passenger) o;
        if (passenger.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), passenger.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Passenger{" +
            "id=" + getId() +
            ", nic='" + getNic() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
