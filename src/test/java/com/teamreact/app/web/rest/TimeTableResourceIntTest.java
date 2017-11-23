package com.teamreact.app.web.rest;

import com.teamreact.app.TicketingSystemApp;

import com.teamreact.app.domain.TimeTable;
import com.teamreact.app.domain.BusRoute;
import com.teamreact.app.repository.TimeTableRepository;
import com.teamreact.app.repository.search.TimeTableSearchRepository;
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
 * Test class for the TimeTableResource REST controller.
 *
 * @see TimeTableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingSystemApp.class)
public class TimeTableResourceIntTest {

    private static final String DEFAULT_STARTING_FROM = "AAAAAAAAAA";
    private static final String UPDATED_STARTING_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_ENDING_FROM = "AAAAAAAAAA";
    private static final String UPDATED_ENDING_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTURE = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTURE = "BBBBBBBBBB";

    private static final String DEFAULT_ARRIVAL = "AAAAAAAAAA";
    private static final String UPDATED_ARRIVAL = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY = "BBBBBBBBBB";

    private static final String DEFAULT_BUS_NO = "AAAAAAAAAA";
    private static final String UPDATED_BUS_NO = "BBBBBBBBBB";

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private TimeTableSearchRepository timeTableSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTimeTableMockMvc;

    private TimeTable timeTable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TimeTableResource timeTableResource = new TimeTableResource(timeTableRepository, timeTableSearchRepository);
        this.restTimeTableMockMvc = MockMvcBuilders.standaloneSetup(timeTableResource)
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
    public static TimeTable createEntity(EntityManager em) {
        TimeTable timeTable = new TimeTable()
            .startingFrom(DEFAULT_STARTING_FROM)
            .endingFrom(DEFAULT_ENDING_FROM)
            .departure(DEFAULT_DEPARTURE)
            .arrival(DEFAULT_ARRIVAL)
            .frequency(DEFAULT_FREQUENCY)
            .busNo(DEFAULT_BUS_NO);
        // Add required entity
        BusRoute route = BusRouteResourceIntTest.createEntity(em);
        em.persist(route);
        em.flush();
        timeTable.getRoutes().add(route);
        return timeTable;
    }

    @Before
    public void initTest() {
        timeTableSearchRepository.deleteAll();
        timeTable = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimeTable() throws Exception {
        int databaseSizeBeforeCreate = timeTableRepository.findAll().size();

        // Create the TimeTable
        restTimeTableMockMvc.perform(post("/api/time-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeTable)))
            .andExpect(status().isCreated());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeCreate + 1);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getStartingFrom()).isEqualTo(DEFAULT_STARTING_FROM);
        assertThat(testTimeTable.getEndingFrom()).isEqualTo(DEFAULT_ENDING_FROM);
        assertThat(testTimeTable.getDeparture()).isEqualTo(DEFAULT_DEPARTURE);
        assertThat(testTimeTable.getArrival()).isEqualTo(DEFAULT_ARRIVAL);
        assertThat(testTimeTable.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);
        assertThat(testTimeTable.getBusNo()).isEqualTo(DEFAULT_BUS_NO);

        // Validate the TimeTable in Elasticsearch
        TimeTable timeTableEs = timeTableSearchRepository.findOne(testTimeTable.getId());
        assertThat(timeTableEs).isEqualToComparingFieldByField(testTimeTable);
    }

    @Test
    @Transactional
    public void createTimeTableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timeTableRepository.findAll().size();

        // Create the TimeTable with an existing ID
        timeTable.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeTableMockMvc.perform(post("/api/time-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeTable)))
            .andExpect(status().isBadRequest());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTimeTables() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);

        // Get all the timeTableList
        restTimeTableMockMvc.perform(get("/api/time-tables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].startingFrom").value(hasItem(DEFAULT_STARTING_FROM.toString())))
            .andExpect(jsonPath("$.[*].endingFrom").value(hasItem(DEFAULT_ENDING_FROM.toString())))
            .andExpect(jsonPath("$.[*].departure").value(hasItem(DEFAULT_DEPARTURE.toString())))
            .andExpect(jsonPath("$.[*].arrival").value(hasItem(DEFAULT_ARRIVAL.toString())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].busNo").value(hasItem(DEFAULT_BUS_NO.toString())));
    }

    @Test
    @Transactional
    public void getTimeTable() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);

        // Get the timeTable
        restTimeTableMockMvc.perform(get("/api/time-tables/{id}", timeTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timeTable.getId().intValue()))
            .andExpect(jsonPath("$.startingFrom").value(DEFAULT_STARTING_FROM.toString()))
            .andExpect(jsonPath("$.endingFrom").value(DEFAULT_ENDING_FROM.toString()))
            .andExpect(jsonPath("$.departure").value(DEFAULT_DEPARTURE.toString()))
            .andExpect(jsonPath("$.arrival").value(DEFAULT_ARRIVAL.toString()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()))
            .andExpect(jsonPath("$.busNo").value(DEFAULT_BUS_NO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTimeTable() throws Exception {
        // Get the timeTable
        restTimeTableMockMvc.perform(get("/api/time-tables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimeTable() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);
        timeTableSearchRepository.save(timeTable);
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();

        // Update the timeTable
        TimeTable updatedTimeTable = timeTableRepository.findOne(timeTable.getId());
        updatedTimeTable
            .startingFrom(UPDATED_STARTING_FROM)
            .endingFrom(UPDATED_ENDING_FROM)
            .departure(UPDATED_DEPARTURE)
            .arrival(UPDATED_ARRIVAL)
            .frequency(UPDATED_FREQUENCY)
            .busNo(UPDATED_BUS_NO);

        restTimeTableMockMvc.perform(put("/api/time-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTimeTable)))
            .andExpect(status().isOk());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getStartingFrom()).isEqualTo(UPDATED_STARTING_FROM);
        assertThat(testTimeTable.getEndingFrom()).isEqualTo(UPDATED_ENDING_FROM);
        assertThat(testTimeTable.getDeparture()).isEqualTo(UPDATED_DEPARTURE);
        assertThat(testTimeTable.getArrival()).isEqualTo(UPDATED_ARRIVAL);
        assertThat(testTimeTable.getFrequency()).isEqualTo(UPDATED_FREQUENCY);
        assertThat(testTimeTable.getBusNo()).isEqualTo(UPDATED_BUS_NO);

        // Validate the TimeTable in Elasticsearch
        TimeTable timeTableEs = timeTableSearchRepository.findOne(testTimeTable.getId());
        assertThat(timeTableEs).isEqualToComparingFieldByField(testTimeTable);
    }

    @Test
    @Transactional
    public void updateNonExistingTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();

        // Create the TimeTable

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTimeTableMockMvc.perform(put("/api/time-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeTable)))
            .andExpect(status().isCreated());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTimeTable() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);
        timeTableSearchRepository.save(timeTable);
        int databaseSizeBeforeDelete = timeTableRepository.findAll().size();

        // Get the timeTable
        restTimeTableMockMvc.perform(delete("/api/time-tables/{id}", timeTable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean timeTableExistsInEs = timeTableSearchRepository.exists(timeTable.getId());
        assertThat(timeTableExistsInEs).isFalse();

        // Validate the database is empty
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTimeTable() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);
        timeTableSearchRepository.save(timeTable);

        // Search the timeTable
        restTimeTableMockMvc.perform(get("/api/_search/time-tables?query=id:" + timeTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].startingFrom").value(hasItem(DEFAULT_STARTING_FROM.toString())))
            .andExpect(jsonPath("$.[*].endingFrom").value(hasItem(DEFAULT_ENDING_FROM.toString())))
            .andExpect(jsonPath("$.[*].departure").value(hasItem(DEFAULT_DEPARTURE.toString())))
            .andExpect(jsonPath("$.[*].arrival").value(hasItem(DEFAULT_ARRIVAL.toString())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].busNo").value(hasItem(DEFAULT_BUS_NO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeTable.class);
        TimeTable timeTable1 = new TimeTable();
        timeTable1.setId(1L);
        TimeTable timeTable2 = new TimeTable();
        timeTable2.setId(timeTable1.getId());
        assertThat(timeTable1).isEqualTo(timeTable2);
        timeTable2.setId(2L);
        assertThat(timeTable1).isNotEqualTo(timeTable2);
        timeTable1.setId(null);
        assertThat(timeTable1).isNotEqualTo(timeTable2);
    }
}
