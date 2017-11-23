package com.teamreact.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teamreact.app.domain.TransportManager;

import com.teamreact.app.repository.TransportManagerRepository;
import com.teamreact.app.repository.search.TransportManagerSearchRepository;
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
 * REST controller for managing TransportManager.
 */
@RestController
@RequestMapping("/api")
public class TransportManagerResource {

    private final Logger log = LoggerFactory.getLogger(TransportManagerResource.class);

    private static final String ENTITY_NAME = "transportManager";

    private final TransportManagerRepository transportManagerRepository;

    private final TransportManagerSearchRepository transportManagerSearchRepository;

    public TransportManagerResource(TransportManagerRepository transportManagerRepository, TransportManagerSearchRepository transportManagerSearchRepository) {
        this.transportManagerRepository = transportManagerRepository;
        this.transportManagerSearchRepository = transportManagerSearchRepository;
    }

    /**
     * POST  /transport-managers : Create a new transportManager.
     *
     * @param transportManager the transportManager to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transportManager, or with status 400 (Bad Request) if the transportManager has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transport-managers")
    @Timed
    public ResponseEntity<TransportManager> createTransportManager(@Valid @RequestBody TransportManager transportManager) throws URISyntaxException {
        log.debug("REST request to save TransportManager : {}", transportManager);
        if (transportManager.getId() != null) {
            throw new BadRequestAlertException("A new transportManager cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransportManager result = transportManagerRepository.save(transportManager);
        transportManagerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/transport-managers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transport-managers : Updates an existing transportManager.
     *
     * @param transportManager the transportManager to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transportManager,
     * or with status 400 (Bad Request) if the transportManager is not valid,
     * or with status 500 (Internal Server Error) if the transportManager couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transport-managers")
    @Timed
    public ResponseEntity<TransportManager> updateTransportManager(@Valid @RequestBody TransportManager transportManager) throws URISyntaxException {
        log.debug("REST request to update TransportManager : {}", transportManager);
        if (transportManager.getId() == null) {
            return createTransportManager(transportManager);
        }
        TransportManager result = transportManagerRepository.save(transportManager);
        transportManagerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transportManager.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transport-managers : get all the transportManagers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transportManagers in body
     */
    @GetMapping("/transport-managers")
    @Timed
    public List<TransportManager> getAllTransportManagers() {
        log.debug("REST request to get all TransportManagers");
        return transportManagerRepository.findAll();
        }

    /**
     * GET  /transport-managers/:id : get the "id" transportManager.
     *
     * @param id the id of the transportManager to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transportManager, or with status 404 (Not Found)
     */
    @GetMapping("/transport-managers/{id}")
    @Timed
    public ResponseEntity<TransportManager> getTransportManager(@PathVariable Long id) {
        log.debug("REST request to get TransportManager : {}", id);
        TransportManager transportManager = transportManagerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transportManager));
    }

    /**
     * DELETE  /transport-managers/:id : delete the "id" transportManager.
     *
     * @param id the id of the transportManager to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transport-managers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransportManager(@PathVariable Long id) {
        log.debug("REST request to delete TransportManager : {}", id);
        transportManagerRepository.delete(id);
        transportManagerSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/transport-managers?query=:query : search for the transportManager corresponding
     * to the query.
     *
     * @param query the query of the transportManager search
     * @return the result of the search
     */
    @GetMapping("/_search/transport-managers")
    @Timed
    public List<TransportManager> searchTransportManagers(@RequestParam String query) {
        log.debug("REST request to search TransportManagers for query {}", query);
        return StreamSupport
            .stream(transportManagerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
