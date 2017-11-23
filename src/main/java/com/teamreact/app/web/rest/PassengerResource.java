package com.teamreact.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teamreact.app.domain.Passenger;

import com.teamreact.app.repository.PassengerRepository;
import com.teamreact.app.repository.search.PassengerSearchRepository;
import com.teamreact.app.web.rest.errors.BadRequestAlertException;
import com.teamreact.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Passenger.
 */
@RestController
@RequestMapping("/api")
public class PassengerResource {

    private final Logger log = LoggerFactory.getLogger(PassengerResource.class);

    private static final String ENTITY_NAME = "passenger";

    private final PassengerRepository passengerRepository;

    private final PassengerSearchRepository passengerSearchRepository;

    public PassengerResource(PassengerRepository passengerRepository, PassengerSearchRepository passengerSearchRepository) {
        this.passengerRepository = passengerRepository;
        this.passengerSearchRepository = passengerSearchRepository;
    }

    /**
     * POST  /passengers : Create a new passenger.
     *
     * @param passenger the passenger to create
     * @return the ResponseEntity with status 201 (Created) and with body the new passenger, or with status 400 (Bad Request) if the passenger has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/passengers")
    @Timed
    public ResponseEntity<Passenger> createPassenger(@Valid @RequestBody Passenger passenger) throws URISyntaxException {
        log.debug("REST request to save Passenger : {}", passenger);
        if (passenger.getId() != null) {
            throw new BadRequestAlertException("A new passenger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Passenger result = passengerRepository.save(passenger);
        passengerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/passengers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /passengers : Updates an existing passenger.
     *
     * @param passenger the passenger to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated passenger,
     * or with status 400 (Bad Request) if the passenger is not valid,
     * or with status 500 (Internal Server Error) if the passenger couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/passengers")
    @Timed
    public ResponseEntity<Passenger> updatePassenger(@Valid @RequestBody Passenger passenger) throws URISyntaxException {
        log.debug("REST request to update Passenger : {}", passenger);
        if (passenger.getId() == null) {
            return createPassenger(passenger);
        }
        Passenger result = passengerRepository.save(passenger);
        passengerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, passenger.getId().toString()))
            .body(result);
    }

    /**
     * GET  /passengers : get all the passengers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of passengers in body
     */
    @GetMapping("/passengers")
    @Timed
    public List<Passenger> getAllPassengers() {
        log.debug("REST request to get all Passengers");
        return passengerRepository.findAll();
        }

    /**
     * GET  /passengers/:id : get the "id" passenger.
     *
     * @param id the id of the passenger to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the passenger, or with status 404 (Not Found)
     */
    @GetMapping("/passengers/{id}")
    @Timed
    public ResponseEntity<Passenger> getPassenger(@PathVariable Long id) {
        log.debug("REST request to get Passenger : {}", id);
        Passenger passenger = passengerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(passenger));
    }

    /**
     * DELETE  /passengers/:id : delete the "id" passenger.
     *
     * @param id the id of the passenger to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/passengers/{id}")
    @Timed
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        log.debug("REST request to delete Passenger : {}", id);
        passengerRepository.delete(id);
        passengerSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/passengers?query=:query : search for the passenger corresponding
     * to the query.
     *
     * @param query the query of the passenger search
     * @return the result of the search
     */
    @GetMapping("/_search/passengers")
    @Timed
    public List<Passenger> searchPassengers(@RequestParam String query) {
        log.debug("REST request to search Passengers for query {}", query);
        return StreamSupport
            .stream(passengerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
