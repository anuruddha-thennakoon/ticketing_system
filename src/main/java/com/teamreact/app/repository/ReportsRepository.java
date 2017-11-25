package com.teamreact.app.repository;

import com.teamreact.app.domain.Reports;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Reports entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportsRepository extends JpaRepository<Reports, Long> {

}
