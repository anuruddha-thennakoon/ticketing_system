package com.teamreact.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teamreact.app.domain.Journey;

import com.teamreact.app.repository.JourneyRepository;
import com.teamreact.app.repository.search.JourneySearchRepository;
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
 * REST controller for managing Journey.
 */
@RestController
@RequestMapping("/api")
public class JourneyResource {

    private final Logger log = LoggerFactory.getLogger(JourneyResource.class);

    private static final String ENTITY_NAME = "journey";

    private final JourneyRepository journeyRepository;

    private final JourneySearchRepository journeySearchRepository;

    public JourneyResource(JourneyRepository journeyRepository, JourneySearchRepository journeySearchRepository) {
        this.journeyRepository = journeyRepository;
        this.journeySearchRepository = journeySearchRepository;
    }

    /**
     * POST  /journeys : Create a new journey.
     *
     * @param journey the journey to create
     * @return the ResponseEntity with status 201 (Created) and with body the new journey, or with status 400 (Bad Request) if the journey has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/journeys")
    @Timed
    public ResponseEntity<Journey> createJourney(@Valid @RequestBody Journey journey) throws URISyntaxException {
        log.debug("REST request to save Journey : {}", journey);
        if (journey.getId() != null) {
            throw new BadRequestAlertException("A new journey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Journey result = journeyRepository.save(journey);
        journeySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/journeys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /journeys : Updates an existing journey.
     *
     * @param journey the journey to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated journey,
     * or with status 400 (Bad Request) if the journey is not valid,
     * or with status 500 (Internal Server Error) if the journey couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/journeys")
    @Timed
    public ResponseEntity<Journey> updateJourney(@Valid @RequestBody Journey journey) throws URISyntaxException {
        log.debug("REST request to update Journey : {}", journey);
        if (journey.getId() == null) {
            return createJourney(journey);
        }
        Journey result = journeyRepository.save(journey);
        journeySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, journey.getId().toString()))
            .body(result);
    }

    /**
     * GET  /journeys : get all the journeys.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of journeys in body
     */
    @GetMapping("/journeys")
    @Timed
    public List<Journey> getAllJourneys() {
        log.debug("REST request to get all Journeys");
        return journeyRepository.findAll();
        }

    /**
     * GET  /journeys/:id : get the "id" journey.
     *
     * @param id the id of the journey to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the journey, or with status 404 (Not Found)
     */
    @GetMapping("/journeys/{id}")
    @Timed
    public ResponseEntity<Journey> getJourney(@PathVariable Long id) {
        log.debug("REST request to get Journey : {}", id);
        Journey journey = journeyRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(journey));
    }

    /**
     * DELETE  /journeys/:id : delete the "id" journey.
     *
     * @param id the id of the journey to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/journeys/{id}")
    @Timed
    public ResponseEntity<Void> deleteJourney(@PathVariable Long id) {
        log.debug("REST request to delete Journey : {}", id);
        journeyRepository.delete(id);
        journeySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/journeys?query=:query : search for the journey corresponding
     * to the query.
     *
     * @param query the query of the journey search
     * @return the result of the search
     */
    @GetMapping("/_search/journeys")
    @Timed
    public List<Journey> searchJourneys(@RequestParam String query) {
        log.debug("REST request to search Journeys for query {}", query);
        return StreamSupport
            .stream(journeySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
