package com.teamreact.app.service;

import com.teamreact.app.domain.Reports;
import com.teamreact.app.repository.ReportsRepository;
import com.teamreact.app.repository.search.ReportsSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Reports.
 */
@Service
@Transactional
public class ReportsService {

    private final Logger log = LoggerFactory.getLogger(ReportsService.class);

    private final ReportsRepository reportsRepository;

    private final ReportsSearchRepository reportsSearchRepository;

    public ReportsService(ReportsRepository reportsRepository, ReportsSearchRepository reportsSearchRepository) {
        this.reportsRepository = reportsRepository;
        this.reportsSearchRepository = reportsSearchRepository;
    }

    /**
     * Save a reports.
     *
     * @param reports the entity to save
     * @return the persisted entity
     */
    public Reports save(Reports reports) {
        log.debug("Request to save Reports : {}", reports);
        Reports result = reportsRepository.save(reports);
        reportsSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the reports.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Reports> findAll() {
        log.debug("Request to get all Reports");
        return reportsRepository.findAll();
    }

    /**
     *  Get one reports by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Reports findOne(Long id) {
        log.debug("Request to get Reports : {}", id);
        return reportsRepository.findOne(id);
    }

    /**
     *  Delete the  reports by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Reports : {}", id);
        reportsRepository.delete(id);
        reportsSearchRepository.delete(id);
    }

    /**
     * Search for the reports corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Reports> search(String query) {
        log.debug("Request to search Reports for query {}", query);
        return StreamSupport
            .stream(reportsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
