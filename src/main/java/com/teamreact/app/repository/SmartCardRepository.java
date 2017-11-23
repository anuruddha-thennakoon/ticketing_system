package com.teamreact.app.repository;

import com.teamreact.app.domain.SmartCard;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SmartCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmartCardRepository extends JpaRepository<SmartCard, Long> {

}
