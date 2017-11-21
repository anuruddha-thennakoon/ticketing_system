package com.teamreact.app.service.impl;

import com.teamreact.app.service.TimeService;
import com.teamreact.app.domain.Time;
import com.teamreact.app.repository.TimeRepository;
import com.teamreact.app.repository.search.TimeSearchRepository;
import com.teamreact.app.service.dto.TimeDTO;
import com.teamreact.app.service.mapper.TimeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Time.
 */
@Service
@Transactional
public class TimeServiceImpl implements TimeService{

    private final Logger log = LoggerFactory.getLogger(TimeServiceImpl.class);

    private final TimeRepository timeRepository;

    private final TimeMapper timeMapper;

    private final TimeSearchRepository timeSearchRepository;

    public TimeServiceImpl(TimeRepository timeRepository, TimeMapper timeMapper, TimeSearchRepository timeSearchRepository) {
        this.timeRepository = timeRepository;
        this.timeMapper = timeMapper;
        this.timeSearchRepository = timeSearchRepository;
    }

    /**
     * Save a time.
     *
     * @param timeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TimeDTO save(TimeDTO timeDTO) {
        log.debug("Request to save Time : {}", timeDTO);
        Time time = timeMapper.toEntity(timeDTO);
        time = timeRepository.save(time);
        TimeDTO result = timeMapper.toDto(time);
        timeSearchRepository.save(time);
        return result;
    }

    /**
     *  Get all the times.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TimeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Times");
        return timeRepository.findAll(pageable)
            .map(timeMapper::toDto);
    }

    /**
     *  Get one time by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TimeDTO findOne(Long id) {
        log.debug("Request to get Time : {}", id);
        Time time = timeRepository.findOne(id);
        return timeMapper.toDto(time);
    }

    /**
     *  Delete the  time by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Time : {}", id);
        timeRepository.delete(id);
        timeSearchRepository.delete(id);
    }

    /**
     * Search for the time corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TimeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Times for query {}", query);
        Page<Time> result = timeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(timeMapper::toDto);
    }
}
