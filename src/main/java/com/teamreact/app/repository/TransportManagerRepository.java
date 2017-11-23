package com.teamreact.app.repository;

import com.teamreact.app.domain.TransportManager;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TransportManager entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransportManagerRepository extends JpaRepository<TransportManager, Long> {

}
