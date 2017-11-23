package com.teamreact.app.web.rest;

import com.teamreact.app.TicketingSystemApp;

import com.teamreact.app.domain.BusRoute;
import com.teamreact.app.domain.Journey;
import com.teamreact.app.domain.TimeTable;
import com.teamreact.app.repository.BusRouteRepository;
import com.teamreact.app.repository.search.BusRouteSearchRepository;
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
 * Test class for the BusRouteResource REST controller.
 *
 * @see BusRouteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingSystemApp.class)
public class BusRouteResourceIntTest {

    private static final String DEFAULT_ROUTE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ROUTE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private BusRouteRepository busRouteRepository;

    @Autowired
    private BusRouteSearchRepository busRouteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBusRouteMockMvc;

    private BusRoute busRoute;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BusRouteResource busRouteResource = new BusRouteResource(busRouteRepository, busRouteSearchRepository);
        this.restBusRouteMockMvc = MockMvcBuilders.standaloneSetup(busRouteResource)
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
    public static BusRoute createEntity(EntityManager em) {
        BusRoute busRoute = new BusRoute()
            .routeNumber(DEFAULT_ROUTE_NUMBER)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Journey journey = JourneyResourceIntTest.createEntity(em);
        em.persist(journey);
        em.flush();
        busRoute.getJourneys().add(journey);
        // Add required entity
        TimeTable timeTable = TimeTableResourceIntTest.createEntity(em);
        em.persist(timeTable);
        em.flush();
        busRoute.getTimeTables().add(timeTable);
        return busRoute;
    }

    @Before
    public void initTest() {
        busRouteSearchRepository.deleteAll();
        busRoute = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusRoute() throws Exception {
        int databaseSizeBeforeCreate = busRouteRepository.findAll().size();

        // Create the BusRoute
        restBusRouteMockMvc.perform(post("/api/bus-routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(busRoute)))
            .andExpect(status().isCreated());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeCreate + 1);
        BusRoute testBusRoute = busRouteList.get(busRouteList.size() - 1);
        assertThat(testBusRoute.getRouteNumber()).isEqualTo(DEFAULT_ROUTE_NUMBER);
        assertThat(testBusRoute.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the BusRoute in Elasticsearch
        BusRoute busRouteEs = busRouteSearchRepository.findOne(testBusRoute.getId());
        assertThat(busRouteEs).isEqualToComparingFieldByField(testBusRoute);
    }

    @Test
    @Transactional
    public void createBusRouteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = busRouteRepository.findAll().size();

        // Create the BusRoute with an existing ID
        busRoute.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusRouteMockMvc.perform(post("/api/bus-routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(busRoute)))
            .andExpect(status().isBadRequest());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBusRoutes() throws Exception {
        // Initialize the database
        busRouteRepository.saveAndFlush(busRoute);

        // Get all the busRouteList
        restBusRouteMockMvc.perform(get("/api/bus-routes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busRoute.getId().intValue())))
            .andExpect(jsonPath("$.[*].routeNumber").value(hasItem(DEFAULT_ROUTE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getBusRoute() throws Exception {
        // Initialize the database
        busRouteRepository.saveAndFlush(busRoute);

        // Get the busRoute
        restBusRouteMockMvc.perform(get("/api/bus-routes/{id}", busRoute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(busRoute.getId().intValue()))
            .andExpect(jsonPath("$.routeNumber").value(DEFAULT_ROUTE_NUMBER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBusRoute() throws Exception {
        // Get the busRoute
        restBusRouteMockMvc.perform(get("/api/bus-routes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusRoute() throws Exception {
        // Initialize the database
        busRouteRepository.saveAndFlush(busRoute);
        busRouteSearchRepository.save(busRoute);
        int databaseSizeBeforeUpdate = busRouteRepository.findAll().size();

        // Update the busRoute
        BusRoute updatedBusRoute = busRouteRepository.findOne(busRoute.getId());
        updatedBusRoute
            .routeNumber(UPDATED_ROUTE_NUMBER)
            .description(UPDATED_DESCRIPTION);

        restBusRouteMockMvc.perform(put("/api/bus-routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBusRoute)))
            .andExpect(status().isOk());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeUpdate);
        BusRoute testBusRoute = busRouteList.get(busRouteList.size() - 1);
        assertThat(testBusRoute.getRouteNumber()).isEqualTo(UPDATED_ROUTE_NUMBER);
        assertThat(testBusRoute.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the BusRoute in Elasticsearch
        BusRoute busRouteEs = busRouteSearchRepository.findOne(testBusRoute.getId());
        assertThat(busRouteEs).isEqualToComparingFieldByField(testBusRoute);
    }

    @Test
    @Transactional
    public void updateNonExistingBusRoute() throws Exception {
        int databaseSizeBeforeUpdate = busRouteRepository.findAll().size();

        // Create the BusRoute

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBusRouteMockMvc.perform(put("/api/bus-routes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(busRoute)))
            .andExpect(status().isCreated());

        // Validate the BusRoute in the database
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBusRoute() throws Exception {
        // Initialize the database
        busRouteRepository.saveAndFlush(busRoute);
        busRouteSearchRepository.save(busRoute);
        int databaseSizeBeforeDelete = busRouteRepository.findAll().size();

        // Get the busRoute
        restBusRouteMockMvc.perform(delete("/api/bus-routes/{id}", busRoute.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean busRouteExistsInEs = busRouteSearchRepository.exists(busRoute.getId());
        assertThat(busRouteExistsInEs).isFalse();

        // Validate the database is empty
        List<BusRoute> busRouteList = busRouteRepository.findAll();
        assertThat(busRouteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBusRoute() throws Exception {
        // Initialize the database
        busRouteRepository.saveAndFlush(busRoute);
        busRouteSearchRepository.save(busRoute);

        // Search the busRoute
        restBusRouteMockMvc.perform(get("/api/_search/bus-routes?query=id:" + busRoute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(busRoute.getId().intValue())))
            .andExpect(jsonPath("$.[*].routeNumber").value(hasItem(DEFAULT_ROUTE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusRoute.class);
        BusRoute busRoute1 = new BusRoute();
        busRoute1.setId(1L);
        BusRoute busRoute2 = new BusRoute();
        busRoute2.setId(busRoute1.getId());
        assertThat(busRoute1).isEqualTo(busRoute2);
        busRoute2.setId(2L);
        assertThat(busRoute1).isNotEqualTo(busRoute2);
        busRoute1.setId(null);
        assertThat(busRoute1).isNotEqualTo(busRoute2);
    }
}
