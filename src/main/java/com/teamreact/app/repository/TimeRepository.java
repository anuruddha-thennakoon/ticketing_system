package com.teamreact.app.repository;

import com.teamreact.app.domain.Time;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Time entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeRepository extends JpaRepository<Time, Long> {

}
