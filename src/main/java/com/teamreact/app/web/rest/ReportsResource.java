package com.teamreact.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teamreact.app.domain.Reports;
import com.teamreact.app.service.ReportsService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Reports.
 */
@RestController
@RequestMapping("/api")
public class ReportsResource {

    private final Logger log = LoggerFactory.getLogger(ReportsResource.class);

    private static final String ENTITY_NAME = "reports";

    private final ReportsService reportsService;

    public ReportsResource(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    /**
     * POST  /reports : Create a new reports.
     *
     * @param reports the reports to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reports, or with status 400 (Bad Request) if the reports has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reports")
    @Timed
    public ResponseEntity<Reports> createReports(@RequestBody Reports reports) throws URISyntaxException {
        log.debug("REST request to save Reports : {}", reports);
        if (reports.getId() != null) {
            throw new BadRequestAlertException("A new reports cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reports result = reportsService.save(reports);
        return ResponseEntity.created(new URI("/api/reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reports : Updates an existing reports.
     *
     * @param reports the reports to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reports,
     * or with status 400 (Bad Request) if the reports is not valid,
     * or with status 500 (Internal Server Error) if the reports couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reports")
    @Timed
    public ResponseEntity<Reports> updateReports(@RequestBody Reports reports) throws URISyntaxException {
        log.debug("REST request to update Reports : {}", reports);
        if (reports.getId() == null) {
            return createReports(reports);
        }
        Reports result = reportsService.save(reports);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reports.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reports : get all the reports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reports in body
     */
    @GetMapping("/reports")
    @Timed
    public List<Reports> getAllReports() {
        log.debug("REST request to get all Reports");
        return reportsService.findAll();
        }

    /**
     * GET  /reports/:id : get the "id" reports.
     *
     * @param id the id of the reports to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reports, or with status 404 (Not Found)
     */
    @GetMapping("/reports/{id}")
    @Timed
    public ResponseEntity<Reports> getReports(@PathVariable Long id) {
        log.debug("REST request to get Reports : {}", id);
        Reports reports = reportsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(reports));
    }

    /**
     * DELETE  /reports/:id : delete the "id" reports.
     *
     * @param id the id of the reports to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteReports(@PathVariable Long id) {
        log.debug("REST request to delete Reports : {}", id);
        reportsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reports?query=:query : search for the reports corresponding
     * to the query.
     *
     * @param query the query of the reports search
     * @return the result of the search
     */
    @GetMapping("/_search/reports")
    @Timed
    public List<Reports> searchReports(@RequestParam String query) {
        log.debug("REST request to search Reports for query {}", query);
        return reportsService.search(query);
    }

}
