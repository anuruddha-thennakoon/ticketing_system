package com.teamreact.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teamreact.app.domain.TimeTable;

import com.teamreact.app.repository.TimeTableRepository;
import com.teamreact.app.repository.search.TimeTableSearchRepository;
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
 * REST controller for managing TimeTable.
 */
@RestController
@RequestMapping("/api")
public class TimeTableResource {

    private final Logger log = LoggerFactory.getLogger(TimeTableResource.class);

    private static final String ENTITY_NAME = "timeTable";

    private final TimeTableRepository timeTableRepository;

    private final TimeTableSearchRepository timeTableSearchRepository;

    public TimeTableResource(TimeTableRepository timeTableRepository, TimeTableSearchRepository timeTableSearchRepository) {
        this.timeTableRepository = timeTableRepository;
        this.timeTableSearchRepository = timeTableSearchRepository;
    }

    /**
     * POST  /time-tables : Create a new timeTable.
     *
     * @param timeTable the timeTable to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timeTable, or with status 400 (Bad Request) if the timeTable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/time-tables")
    @Timed
    public ResponseEntity<TimeTable> createTimeTable(@Valid @RequestBody TimeTable timeTable) throws URISyntaxException {
        log.debug("REST request to save TimeTable : {}", timeTable);
        if (timeTable.getId() != null) {
            throw new BadRequestAlertException("A new timeTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TimeTable result = timeTableRepository.save(timeTable);
        timeTableSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/time-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /time-tables : Updates an existing timeTable.
     *
     * @param timeTable the timeTable to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timeTable,
     * or with status 400 (Bad Request) if the timeTable is not valid,
     * or with status 500 (Internal Server Error) if the timeTable couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/time-tables")
    @Timed
    public ResponseEntity<TimeTable> updateTimeTable(@Valid @RequestBody TimeTable timeTable) throws URISyntaxException {
        log.debug("REST request to update TimeTable : {}", timeTable);
        if (timeTable.getId() == null) {
            return createTimeTable(timeTable);
        }
        TimeTable result = timeTableRepository.save(timeTable);
        timeTableSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timeTable.getId().toString()))
            .body(result);
    }

    /**
     * GET  /time-tables : get all the timeTables.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of timeTables in body
     */
    @GetMapping("/time-tables")
    @Timed
    public List<TimeTable> getAllTimeTables() {
        log.debug("REST request to get all TimeTables");
        return timeTableRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /time-tables/:id : get the "id" timeTable.
     *
     * @param id the id of the timeTable to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timeTable, or with status 404 (Not Found)
     */
    @GetMapping("/time-tables/{id}")
    @Timed
    public ResponseEntity<TimeTable> getTimeTable(@PathVariable Long id) {
        log.debug("REST request to get TimeTable : {}", id);
        TimeTable timeTable = timeTableRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(timeTable));
    }

    /**
     * DELETE  /time-tables/:id : delete the "id" timeTable.
     *
     * @param id the id of the timeTable to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/time-tables/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimeTable(@PathVariable Long id) {
        log.debug("REST request to delete TimeTable : {}", id);
        timeTableRepository.delete(id);
        timeTableSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/time-tables?query=:query : search for the timeTable corresponding
     * to the query.
     *
     * @param query the query of the timeTable search
     * @return the result of the search
     */
    @GetMapping("/_search/time-tables")
    @Timed
    public List<TimeTable> searchTimeTables(@RequestParam String query) {
        log.debug("REST request to search TimeTables for query {}", query);
        return StreamSupport
            .stream(timeTableSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
