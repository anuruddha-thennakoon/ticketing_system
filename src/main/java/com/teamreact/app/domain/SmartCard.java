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
 * A SmartCard.
 */
@Entity
@Table(name = "smart_card")
@Document(indexName = "smartcard")
public class SmartCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_balance")
    private Integer cardBalance;

    @Column(name = "issued_date")
    private String issuedDate;

    @Column(name = "card_number")
    private String cardNumber;

    @OneToMany(mappedBy = "smartCard")
    @JsonIgnore
    private Set<Journey> journeys = new HashSet<>();

    @OneToMany(mappedBy = "smartCard")
    @JsonIgnore
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(mappedBy = "smartCard")
    @JsonIgnore
    private Set<Recharge> recharges = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCardBalance() {
        return cardBalance;
    }

    public SmartCard cardBalance(Integer cardBalance) {
        this.cardBalance = cardBalance;
        return this;
    }

    public void setCardBalance(Integer cardBalance) {
        this.cardBalance = cardBalance;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public SmartCard issuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
        return this;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public SmartCard cardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Set<Journey> getJourneys() {
        return journeys;
    }

    public SmartCard journeys(Set<Journey> journeys) {
        this.journeys = journeys;
        return this;
    }

    public SmartCard addJourney(Journey journey) {
        this.journeys.add(journey);
        journey.setSmartCard(this);
        return this;
    }

    public SmartCard removeJourney(Journey journey) {
        this.journeys.remove(journey);
        journey.setSmartCard(null);
        return this;
    }

    public void setJourneys(Set<Journey> journeys) {
        this.journeys = journeys;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public SmartCard payments(Set<Payment> payments) {
        this.payments = payments;
        return this;
    }

    public SmartCard addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setSmartCard(this);
        return this;
    }

    public SmartCard removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setSmartCard(null);
        return this;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public Set<Recharge> getRecharges() {
        return recharges;
    }

    public SmartCard recharges(Set<Recharge> recharges) {
        this.recharges = recharges;
        return this;
    }

    public SmartCard addRecharge(Recharge recharge) {
        this.recharges.add(recharge);
        recharge.setSmartCard(this);
        return this;
    }

    public SmartCard removeRecharge(Recharge recharge) {
        this.recharges.remove(recharge);
        recharge.setSmartCard(null);
        return this;
    }

    public void setRecharges(Set<Recharge> recharges) {
        this.recharges = recharges;
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
        SmartCard smartCard = (SmartCard) o;
        if (smartCard.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smartCard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmartCard{" +
            "id=" + getId() +
            ", cardBalance='" + getCardBalance() + "'" +
            ", issuedDate='" + getIssuedDate() + "'" +
            ", cardNumber='" + getCardNumber() + "'" +
            "}";
    }
}
