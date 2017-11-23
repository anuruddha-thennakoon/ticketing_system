package com.teamreact.app.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Recharge.
 */
@Entity
@Table(name = "recharge")
@Document(indexName = "recharge")
public class Recharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recharge_method")
    private String rechargeMethod;

    @Column(name = "recharge_amount")
    private Integer rechargeAmount;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "exp_date")
    private String expDate;

    @ManyToOne
    private SmartCard smartCard;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRechargeMethod() {
        return rechargeMethod;
    }

    public Recharge rechargeMethod(String rechargeMethod) {
        this.rechargeMethod = rechargeMethod;
        return this;
    }

    public void setRechargeMethod(String rechargeMethod) {
        this.rechargeMethod = rechargeMethod;
    }

    public Integer getRechargeAmount() {
        return rechargeAmount;
    }

    public Recharge rechargeAmount(Integer rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
        return this;
    }

    public void setRechargeAmount(Integer rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Recharge accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public Recharge cardType(String cardType) {
        this.cardType = cardType;
        return this;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getExpDate() {
        return expDate;
    }

    public Recharge expDate(String expDate) {
        this.expDate = expDate;
        return this;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public SmartCard getSmartCard() {
        return smartCard;
    }

    public Recharge smartCard(SmartCard smartCard) {
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
        Recharge recharge = (Recharge) o;
        if (recharge.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recharge.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Recharge{" +
            "id=" + getId() +
            ", rechargeMethod='" + getRechargeMethod() + "'" +
            ", rechargeAmount='" + getRechargeAmount() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", cardType='" + getCardType() + "'" +
            ", expDate='" + getExpDate() + "'" +
            "}";
    }
}
