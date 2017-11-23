package com.teamreact.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teamreact.app.domain.BusRoute;

import com.teamreact.app.repository.BusRouteRepository;
import com.teamreact.app.repository.search.BusRouteSearchRepository;
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
 * REST controller for managing BusRoute.
 */
@RestController
@RequestMapping("/api")
public class BusRouteResource {

    private final Logger log = LoggerFactory.getLogger(BusRouteResource.class);

    private static final String ENTITY_NAME = "busRoute";

    private final BusRouteRepository busRouteRepository;

    private final BusRouteSearchRepository busRouteSearchRepository;

    public BusRouteResource(BusRouteRepository busRouteRepository, BusRouteSearchRepository busRouteSearchRepository) {
        this.busRouteRepository = busRouteRepository;
        this.busRouteSearchRepository = busRouteSearchRepository;
    }

    /**
     * POST  /bus-routes : Create a new busRoute.
     *
     * @param busRoute the busRoute to create
     * @return the ResponseEntity with status 201 (Created) and with body the new busRoute, or with status 400 (Bad Request) if the busRoute has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bus-routes")
    @Timed
    public ResponseEntity<BusRoute> createBusRoute(@Valid @RequestBody BusRoute busRoute) throws URISyntaxException {
        log.debug("REST request to save BusRoute : {}", busRoute);
        if (busRoute.getId() != null) {
            throw new BadRequestAlertException("A new busRoute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusRoute result = busRouteRepository.save(busRoute);
        busRouteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bus-routes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bus-routes : Updates an existing busRoute.
     *
     * @param busRoute the busRoute to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated busRoute,
     * or with status 400 (Bad Request) if the busRoute is not valid,
     * or with status 500 (Internal Server Error) if the busRoute couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bus-routes")
    @Timed
    public ResponseEntity<BusRoute> updateBusRoute(@Valid @RequestBody BusRoute busRoute) throws URISyntaxException {
        log.debug("REST request to update BusRoute : {}", busRoute);
        if (busRoute.getId() == null) {
            return createBusRoute(busRoute);
        }
        BusRoute result = busRouteRepository.save(busRoute);
        busRouteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, busRoute.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bus-routes : get all the busRoutes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of busRoutes in body
     */
    @GetMapping("/bus-routes")
    @Timed
    public List<BusRoute> getAllBusRoutes() {
        log.debug("REST request to get all BusRoutes");
        return busRouteRepository.findAll();
        }

    /**
     * GET  /bus-routes/:id : get the "id" busRoute.
     *
     * @param id the id of the busRoute to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the busRoute, or with status 404 (Not Found)
     */
    @GetMapping("/bus-routes/{id}")
    @Timed
    public ResponseEntity<BusRoute> getBusRoute(@PathVariable Long id) {
        log.debug("REST request to get BusRoute : {}", id);
        BusRoute busRoute = busRouteRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(busRoute));
    }

    /**
     * DELETE  /bus-routes/:id : delete the "id" busRoute.
     *
     * @param id the id of the busRoute to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bus-routes/{id}")
    @Timed
    public ResponseEntity<Void> deleteBusRoute(@PathVariable Long id) {
        log.debug("REST request to delete BusRoute : {}", id);
        busRouteRepository.delete(id);
        busRouteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bus-routes?query=:query : search for the busRoute corresponding
     * to the query.
     *
     * @param query the query of the busRoute search
     * @return the result of the search
     */
    @GetMapping("/_search/bus-routes")
    @Timed
    public List<BusRoute> searchBusRoutes(@RequestParam String query) {
        log.debug("REST request to search BusRoutes for query {}", query);
        return StreamSupport
            .stream(busRouteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
