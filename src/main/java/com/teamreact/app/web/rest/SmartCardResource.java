package com.teamreact.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teamreact.app.domain.SmartCard;

import com.teamreact.app.repository.SmartCardRepository;
import com.teamreact.app.repository.search.SmartCardSearchRepository;
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
 * REST controller for managing SmartCard.
 */
@RestController
@RequestMapping("/api")
public class SmartCardResource {

    private final Logger log = LoggerFactory.getLogger(SmartCardResource.class);

    private static final String ENTITY_NAME = "smartCard";

    private final SmartCardRepository smartCardRepository;

    private final SmartCardSearchRepository smartCardSearchRepository;

    public SmartCardResource(SmartCardRepository smartCardRepository, SmartCardSearchRepository smartCardSearchRepository) {
        this.smartCardRepository = smartCardRepository;
        this.smartCardSearchRepository = smartCardSearchRepository;
    }

    /**
     * POST  /smart-cards : Create a new smartCard.
     *
     * @param smartCard the smartCard to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smartCard, or with status 400 (Bad Request) if the smartCard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smart-cards")
    @Timed
    public ResponseEntity<SmartCard> createSmartCard(@Valid @RequestBody SmartCard smartCard) throws URISyntaxException {
        log.debug("REST request to save SmartCard : {}", smartCard);
        if (smartCard.getId() != null) {
            throw new BadRequestAlertException("A new smartCard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmartCard result = smartCardRepository.save(smartCard);
        smartCardSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/smart-cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smart-cards : Updates an existing smartCard.
     *
     * @param smartCard the smartCard to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smartCard,
     * or with status 400 (Bad Request) if the smartCard is not valid,
     * or with status 500 (Internal Server Error) if the smartCard couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smart-cards")
    @Timed
    public ResponseEntity<SmartCard> updateSmartCard(@Valid @RequestBody SmartCard smartCard) throws URISyntaxException {
        log.debug("REST request to update SmartCard : {}", smartCard);
        if (smartCard.getId() == null) {
            return createSmartCard(smartCard);
        }
        SmartCard result = smartCardRepository.save(smartCard);
        smartCardSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smartCard.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smart-cards : get all the smartCards.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smartCards in body
     */
    @GetMapping("/smart-cards")
    @Timed
    public List<SmartCard> getAllSmartCards() {
        log.debug("REST request to get all SmartCards");
        return smartCardRepository.findAll();
        }

    /**
     * GET  /smart-cards/:id : get the "id" smartCard.
     *
     * @param id the id of the smartCard to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smartCard, or with status 404 (Not Found)
     */
    @GetMapping("/smart-cards/{id}")
    @Timed
    public ResponseEntity<SmartCard> getSmartCard(@PathVariable Long id) {
        log.debug("REST request to get SmartCard : {}", id);
        SmartCard smartCard = smartCardRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(smartCard));
    }

    /**
     * DELETE  /smart-cards/:id : delete the "id" smartCard.
     *
     * @param id the id of the smartCard to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smart-cards/{id}")
    @Timed
    public ResponseEntity<Void> deleteSmartCard(@PathVariable Long id) {
        log.debug("REST request to delete SmartCard : {}", id);
        smartCardRepository.delete(id);
        smartCardSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/smart-cards?query=:query : search for the smartCard corresponding
     * to the query.
     *
     * @param query the query of the smartCard search
     * @return the result of the search
     */
    @GetMapping("/_search/smart-cards")
    @Timed
    public List<SmartCard> searchSmartCards(@RequestParam String query) {
        log.debug("REST request to search SmartCards for query {}", query);
        return StreamSupport
            .stream(smartCardSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
