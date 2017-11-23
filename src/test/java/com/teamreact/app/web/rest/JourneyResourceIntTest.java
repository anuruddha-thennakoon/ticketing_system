package com.teamreact.app.web.rest;

import com.teamreact.app.TicketingSystemApp;

import com.teamreact.app.domain.Journey;
import com.teamreact.app.domain.Payment;
import com.teamreact.app.domain.BusRoute;
import com.teamreact.app.repository.JourneyRepository;
import com.teamreact.app.repository.search.JourneySearchRepository;
import com.teamreact.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.teamreact.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JourneyResource REST controller.
 *
 * @see JourneyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingSystemApp.class)
public class JourneyResourceIntTest {

    private static final String DEFAULT_JOURNEY_DATE = "AAAAAAAAAA";
    private static final String UPDATED_JOURNEY_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_STARTING_POINT = "AAAAAAAAAA";
    private static final String UPDATED_STARTING_POINT = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTURE_TIME = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTURE_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_BUS_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BUS_NUMBER = "BBBBBBBBBB";

    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private JourneySearchRepository journeySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJourneyMockMvc;

    private Journey journey;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JourneyResource journeyResource = new JourneyResource(journeyRepository, journeySearchRepository);
        this.restJourneyMockMvc = MockMvcBuilders.standaloneSetup(journeyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journey createEntity(EntityManager em) {
        Journey journey = new Journey()
            .journeyDate(DEFAULT_JOURNEY_DATE)
            .startingPoint(DEFAULT_STARTING_POINT)
            .destination(DEFAULT_DESTINATION)
            .departureTime(DEFAULT_DEPARTURE_TIME)
            .busNumber(DEFAULT_BUS_NUMBER);
        // Add required entity
        Payment payment = PaymentResourceIntTest.createEntity(em);
        em.persist(payment);
        em.flush();
        journey.setPayment(payment);
        // Add required entity
        BusRoute route = BusRouteResourceIntTest.createEntity(em);
        em.persist(route);
        em.flush();
        journey.setRoute(route);
        return journey;
    }

    @Before
    public void initTest() {
        journeySearchRepository.deleteAll();
        journey = createEntity(em);
    }

    @Test
    @Transactional
    public void createJourney() throws Exception {
        int databaseSizeBeforeCreate = journeyRepository.findAll().size();

        // Create the Journey
        restJourneyMockMvc.perform(post("/api/journeys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journey)))
            .andExpect(status().isCreated());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeCreate + 1);
        Journey testJourney = journeyList.get(journeyList.size() - 1);
        assertThat(testJourney.getJourneyDate()).isEqualTo(DEFAULT_JOURNEY_DATE);
        assertThat(testJourney.getStartingPoint()).isEqualTo(DEFAULT_STARTING_POINT);
        assertThat(testJourney.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testJourney.getDepartureTime()).isEqualTo(DEFAULT_DEPARTURE_TIME);
        assertThat(testJourney.getBusNumber()).isEqualTo(DEFAULT_BUS_NUMBER);

        // Validate the Journey in Elasticsearch
        Journey journeyEs = journeySearchRepository.findOne(testJourney.getId());
        assertThat(journeyEs).isEqualToComparingFieldByField(testJourney);
    }

    @Test
    @Transactional
    public void createJourneyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = journeyRepository.findAll().size();

        // Create the Journey with an existing ID
        journey.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJourneyMockMvc.perform(post("/api/journeys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journey)))
            .andExpect(status().isBadRequest());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllJourneys() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList
        restJourneyMockMvc.perform(get("/api/journeys?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journey.getId().intValue())))
            .andExpect(jsonPath("$.[*].journeyDate").value(hasItem(DEFAULT_JOURNEY_DATE.toString())))
            .andExpect(jsonPath("$.[*].startingPoint").value(hasItem(DEFAULT_STARTING_POINT.toString())))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION.toString())))
            .andExpect(jsonPath("$.[*].departureTime").value(hasItem(DEFAULT_DEPARTURE_TIME.toString())))
            .andExpect(jsonPath("$.[*].busNumber").value(hasItem(DEFAULT_BUS_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getJourney() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get the journey
        restJourneyMockMvc.perform(get("/api/journeys/{id}", journey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(journey.getId().intValue()))
            .andExpect(jsonPath("$.journeyDate").value(DEFAULT_JOURNEY_DATE.toString()))
            .andExpect(jsonPath("$.startingPoint").value(DEFAULT_STARTING_POINT.toString()))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION.toString()))
            .andExpect(jsonPath("$.departureTime").value(DEFAULT_DEPARTURE_TIME.toString()))
            .andExpect(jsonPath("$.busNumber").value(DEFAULT_BUS_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJourney() throws Exception {
        // Get the journey
        restJourneyMockMvc.perform(get("/api/journeys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJourney() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);
        journeySearchRepository.save(journey);
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();

        // Update the journey
        Journey updatedJourney = journeyRepository.findOne(journey.getId());
        updatedJourney
            .journeyDate(UPDATED_JOURNEY_DATE)
            .startingPoint(UPDATED_STARTING_POINT)
            .destination(UPDATED_DESTINATION)
            .departureTime(UPDATED_DEPARTURE_TIME)
            .busNumber(UPDATED_BUS_NUMBER);

        restJourneyMockMvc.perform(put("/api/journeys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJourney)))
            .andExpect(status().isOk());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
        Journey testJourney = journeyList.get(journeyList.size() - 1);
        assertThat(testJourney.getJourneyDate()).isEqualTo(UPDATED_JOURNEY_DATE);
        assertThat(testJourney.getStartingPoint()).isEqualTo(UPDATED_STARTING_POINT);
        assertThat(testJourney.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testJourney.getDepartureTime()).isEqualTo(UPDATED_DEPARTURE_TIME);
        assertThat(testJourney.getBusNumber()).isEqualTo(UPDATED_BUS_NUMBER);

        // Validate the Journey in Elasticsearch
        Journey journeyEs = journeySearchRepository.findOne(testJourney.getId());
        assertThat(journeyEs).isEqualToComparingFieldByField(testJourney);
    }

    @Test
    @Transactional
    public void updateNonExistingJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();

        // Create the Journey

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJourneyMockMvc.perform(put("/api/journeys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(journey)))
            .andExpect(status().isCreated());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJourney() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);
        journeySearchRepository.save(journey);
        int databaseSizeBeforeDelete = journeyRepository.findAll().size();

        // Get the journey
        restJourneyMockMvc.perform(delete("/api/journeys/{id}", journey.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean journeyExistsInEs = journeySearchRepository.exists(journey.getId());
        assertThat(journeyExistsInEs).isFalse();

        // Validate the database is empty
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJourney() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);
        journeySearchRepository.save(journey);

        // Search the journey
        restJourneyMockMvc.perform(get("/api/_search/journeys?query=id:" + journey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journey.getId().intValue())))
            .andExpect(jsonPath("$.[*].journeyDate").value(hasItem(DEFAULT_JOURNEY_DATE.toString())))
            .andExpect(jsonPath("$.[*].startingPoint").value(hasItem(DEFAULT_STARTING_POINT.toString())))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION.toString())))
            .andExpect(jsonPath("$.[*].departureTime").value(hasItem(DEFAULT_DEPARTURE_TIME.toString())))
            .andExpect(jsonPath("$.[*].busNumber").value(hasItem(DEFAULT_BUS_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Journey.class);
        Journey journey1 = new Journey();
        journey1.setId(1L);
        Journey journey2 = new Journey();
        journey2.setId(journey1.getId());
        assertThat(journey1).isEqualTo(journey2);
        journey2.setId(2L);
        assertThat(journey1).isNotEqualTo(journey2);
        journey1.setId(null);
        assertThat(journey1).isNotEqualTo(journey2);
    }
}
