package com.teamreact.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Document(indexName = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "payment_date")
    private String paymentDate;

    @OneToOne(mappedBy = "payment")
    @JsonIgnore
    private Journey journey;

    @ManyToOne
    private SmartCard smartCard;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public Payment amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public Payment paymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Journey getJourney() {
        return journey;
    }

    public Payment journey(Journey journey) {
        this.journey = journey;
        return this;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public SmartCard getSmartCard() {
        return smartCard;
    }

    public Payment smartCard(SmartCard smartCard) {
        this.smartCard = smartCard;
        return this;
    }

    public void setSmartCard(SmartCard smartCard) {
        this.smartCard = smartCard;
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
        Payment payment = (Payment) o;
        if (payment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", amount='" + getAmount() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            "}";
    }
}
