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
 * A TransportManager.
 */
@Entity
@Table(name = "transport_manager")
@Document(indexName = "transportmanager")
public class TransportManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "nic")
    private String nic;

    @OneToMany(mappedBy = "transportManager")
    @JsonIgnore
    private Set<TimeTable> timeTables = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TransportManager name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public TransportManager nic(String nic) {
        this.nic = nic;
        return this;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public Set<TimeTable> getTimeTables() {
        return timeTables;
    }

    public TransportManager timeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
        return this;
    }

    public TransportManager addTimeTable(TimeTable timeTable) {
        this.timeTables.add(timeTable);
        timeTable.setTransportManager(this);
        return this;
    }

    public TransportManager removeTimeTable(TimeTable timeTable) {
        this.timeTables.remove(timeTable);
        timeTable.setTransportManager(null);
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
        TransportManager transportManager = (TransportManager) o;
        if (transportManager.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transportManager.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransportManager{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nic='" + getNic() + "'" +
            "}";
    }
}
