package com.teamreact.app.repository;

import com.teamreact.app.domain.Recharge;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Recharge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {

}
