package com.teamreact.app.service;

import com.teamreact.app.service.dto.TimeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Time.
 */
public interface TimeService {

    /**
     * Save a time.
     *
     * @param timeDTO the entity to save
     * @return the persisted entity
     */
    TimeDTO save(TimeDTO timeDTO);

    /**
     *  Get all the times.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TimeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" time.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TimeDTO findOne(Long id);

    /**
     *  Delete the "id" time.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the time corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TimeDTO> search(String query, Pageable pageable);
}
