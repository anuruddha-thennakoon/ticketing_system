package com.teamreact.app.repository;

import com.teamreact.app.domain.BusRoute;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BusRoute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, Long> {

}
