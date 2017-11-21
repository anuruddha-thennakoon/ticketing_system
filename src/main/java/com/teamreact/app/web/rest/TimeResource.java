package com.teamreact.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teamreact.app.service.TimeService;
import com.teamreact.app.web.rest.errors.BadRequestAlertException;
import com.teamreact.app.web.rest.util.HeaderUtil;
import com.teamreact.app.web.rest.util.PaginationUtil;
import com.teamreact.app.service.dto.TimeDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Time.
 */
@RestController
@RequestMapping("/api")
public class TimeResource {

    private final Logger log = LoggerFactory.getLogger(TimeResource.class);

    private static final String ENTITY_NAME = "time";

    private final TimeService timeService;

    public TimeResource(TimeService timeService) {
        this.timeService = timeService;
    }

    /**
     * POST  /times : Create a new time.
     *
     * @param timeDTO the timeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timeDTO, or with status 400 (Bad Request) if the time has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/times")
    @Timed
    public ResponseEntity<TimeDTO> createTime(@Valid @RequestBody TimeDTO timeDTO) throws URISyntaxException {
        log.debug("REST request to save Time : {}", timeDTO);
        if (timeDTO.getId() != null) {
            throw new BadRequestAlertException("A new time cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TimeDTO result = timeService.save(timeDTO);
        return ResponseEntity.created(new URI("/api/times/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /times : Updates an existing time.
     *
     * @param timeDTO the timeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timeDTO,
     * or with status 400 (Bad Request) if the timeDTO is not valid,
     * or with status 500 (Internal Server Error) if the timeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/times")
    @Timed
    public ResponseEntity<TimeDTO> updateTime(@Valid @RequestBody TimeDTO timeDTO) throws URISyntaxException {
        log.debug("REST request to update Time : {}", timeDTO);
        if (timeDTO.getId() == null) {
            return createTime(timeDTO);
        }
        TimeDTO result = timeService.save(timeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /times : get all the times.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of times in body
     */
    @GetMapping("/times")
    @Timed
    public ResponseEntity<List<TimeDTO>> getAllTimes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Times");
        Page<TimeDTO> page = timeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/times");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /times/:id : get the "id" time.
     *
     * @param id the id of the timeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/times/{id}")
    @Timed
    public ResponseEntity<TimeDTO> getTime(@PathVariable Long id) {
        log.debug("REST request to get Time : {}", id);
        TimeDTO timeDTO = timeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(timeDTO));
    }

    /**
     * DELETE  /times/:id : delete the "id" time.
     *
     * @param id the id of the timeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/times/{id}")
    @Timed
    public ResponseEntity<Void> deleteTime(@PathVariable Long id) {
        log.debug("REST request to delete Time : {}", id);
        timeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/times?query=:query : search for the time corresponding
     * to the query.
     *
     * @param query the query of the time search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/times")
    @Timed
    public ResponseEntity<List<TimeDTO>> searchTimes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Times for query {}", query);
        Page<TimeDTO> page = timeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/times");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
