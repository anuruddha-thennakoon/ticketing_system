package com.teamreact.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teamreact.app.domain.Recharge;

import com.teamreact.app.repository.RechargeRepository;
import com.teamreact.app.repository.search.RechargeSearchRepository;
import com.teamreact.app.web.rest.errors.BadRequestAlertException;
import com.teamreact.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Recharge.
 */
@RestController
@RequestMapping("/api")
public class RechargeResource {

    private final Logger log = LoggerFactory.getLogger(RechargeResource.class);

    private static final String ENTITY_NAME = "recharge";

    private final RechargeRepository rechargeRepository;

    private final RechargeSearchRepository rechargeSearchRepository;

    public RechargeResource(RechargeRepository rechargeRepository, RechargeSearchRepository rechargeSearchRepository) {
        this.rechargeRepository = rechargeRepository;
        this.rechargeSearchRepository = rechargeSearchRepository;
    }

    /**
     * POST  /recharges : Create a new recharge.
     *
     * @param recharge the recharge to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recharge, or with status 400 (Bad Request) if the recharge has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recharges")
    @Timed
    public ResponseEntity<Recharge> createRecharge(@RequestBody Recharge recharge) throws URISyntaxException {
        log.debug("REST request to save Recharge : {}", recharge);
        if (recharge.getId() != null) {
            throw new BadRequestAlertException("A new recharge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Recharge result = rechargeRepository.save(recharge);
        rechargeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/recharges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recharges : Updates an existing recharge.
     *
     * @param recharge the recharge to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recharge,
     * or with status 400 (Bad Request) if the recharge is not valid,
     * or with status 500 (Internal Server Error) if the recharge couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recharges")
    @Timed
    public ResponseEntity<Recharge> updateRecharge(@RequestBody Recharge recharge) throws URISyntaxException {
        log.debug("REST request to update Recharge : {}", recharge);
        if (recharge.getId() == null) {
            return createRecharge(recharge);
        }
        Recharge result = rechargeRepository.save(recharge);
        rechargeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recharge.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recharges : get all the recharges.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of recharges in body
     */
    @GetMapping("/recharges")
    @Timed
    public List<Recharge> getAllRecharges() {
        log.debug("REST request to get all Recharges");
        return rechargeRepository.findAll();
        }

    /**
     * GET  /recharges/:id : get the "id" recharge.
     *
     * @param id the id of the recharge to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recharge, or with status 404 (Not Found)
     */
    @GetMapping("/recharges/{id}")
    @Timed
    public ResponseEntity<Recharge> getRecharge(@PathVariable Long id) {
        log.debug("REST request to get Recharge : {}", id);
        Recharge recharge = rechargeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(recharge));
    }

    /**
     * DELETE  /recharges/:id : delete the "id" recharge.
     *
     * @param id the id of the recharge to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recharges/{id}")
    @Timed
    public ResponseEntity<Void> deleteRecharge(@PathVariable Long id) {
        log.debug("REST request to delete Recharge : {}", id);
        rechargeRepository.delete(id);
        rechargeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/recharges?query=:query : search for the recharge corresponding
     * to the query.
     *
     * @param query the query of the recharge search
     * @return the result of the search
     */
    @GetMapping("/_search/recharges")
    @Timed
    public List<Recharge> searchRecharges(@RequestParam String query) {
        log.debug("REST request to search Recharges for query {}", query);
        return StreamSupport
            .stream(rechargeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
