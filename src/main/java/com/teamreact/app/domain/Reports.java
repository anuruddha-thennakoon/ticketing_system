package com.teamreact.app.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Reports.
 */
@Entity
@Table(name = "reports")
@Document(indexName = "reports")
public class Reports implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_name")
    private String reportName;

    @Column(name = "report_type")
    private String reportType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public Reports reportName(String reportName) {
        this.reportName = reportName;
        return this;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportType() {
        return reportType;
    }

    public Reports reportType(String reportType) {
        this.reportType = reportType;
        return this;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
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
        Reports reports = (Reports) o;
        if (reports.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reports.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reports{" +
            "id=" + getId() +
            ", reportName='" + getReportName() + "'" +
            ", reportType='" + getReportType() + "'" +
            "}";
    }
}
