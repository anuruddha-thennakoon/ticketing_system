package com.teamreact.app.web.rest;

import com.teamreact.app.TicketingSystemApp;

import com.teamreact.app.domain.Time;
import com.teamreact.app.repository.TimeRepository;
import com.teamreact.app.service.TimeService;
import com.teamreact.app.repository.search.TimeSearchRepository;
import com.teamreact.app.service.dto.TimeDTO;
import com.teamreact.app.service.mapper.TimeMapper;
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
 * Test class for the TimeResource REST controller.
 *
 * @see TimeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingSystemApp.class)
public class TimeResourceIntTest {

    private static final String DEFAULT_ROUTE = "AAAAAAAAAA";
    private static final String UPDATED_ROUTE = "BBBBBBBBBB";

    private static final String DEFAULT_BUS_NO = "AAAAAAAAAA";
    private static final String UPDATED_BUS_NO = "BBBBBBBBBB";

    private static final String DEFAULT_FROM = "AAAAAAAAAA";
    private static final String UPDATED_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_TO = "AAAAAAAAAA";
    private static final String UPDATED_TO = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTURE = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTURE = "BBBBBBBBBB";

    private static final String DEFAULT_ARRIVAL = "AAAAAAAAAA";
    private static final String UPDATED_ARRIVAL = "BBBBBBBBBB";

    private static final String DEFAULT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_FREQUENCY = "BBBBBBBBBB";

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private TimeMapper timeMapper;

    @Autowired
    private TimeService timeService;

    @Autowired
    private TimeSearchRepository timeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTimeMockMvc;

    private Time time;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TimeResource timeResource = new TimeResource(timeService);
        this.restTimeMockMvc = MockMvcBuilders.standaloneSetup(timeResource)
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
    public static Time createEntity(EntityManager em) {
        Time time = new Time()
            .route(DEFAULT_ROUTE)
            .bus_no(DEFAULT_BUS_NO)
            .from(DEFAULT_FROM)
            .to(DEFAULT_TO)
            .departure(DEFAULT_DEPARTURE)
            .arrival(DEFAULT_ARRIVAL)
            .frequency(DEFAULT_FREQUENCY);
        return time;
    }

    @Before
    public void initTest() {
        timeSearchRepository.deleteAll();
        time = createEntity(em);
    }

    @Test
    @Transactional
    public void createTime() throws Exception {
        int databaseSizeBeforeCreate = timeRepository.findAll().size();

        // Create the Time
        TimeDTO timeDTO = timeMapper.toDto(time);
        restTimeMockMvc.perform(post("/api/times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeDTO)))
            .andExpect(status().isCreated());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeCreate + 1);
        Time testTime = timeList.get(timeList.size() - 1);
        assertThat(testTime.getRoute()).isEqualTo(DEFAULT_ROUTE);
        assertThat(testTime.getBus_no()).isEqualTo(DEFAULT_BUS_NO);
        assertThat(testTime.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testTime.getTo()).isEqualTo(DEFAULT_TO);
        assertThat(testTime.getDeparture()).isEqualTo(DEFAULT_DEPARTURE);
        assertThat(testTime.getArrival()).isEqualTo(DEFAULT_ARRIVAL);
        assertThat(testTime.getFrequency()).isEqualTo(DEFAULT_FREQUENCY);

        // Validate the Time in Elasticsearch
        Time timeEs = timeSearchRepository.findOne(testTime.getId());
        assertThat(timeEs).isEqualToComparingFieldByField(testTime);
    }

    @Test
    @Transactional
    public void createTimeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timeRepository.findAll().size();

        // Create the Time with an existing ID
        time.setId(1L);
        TimeDTO timeDTO = timeMapper.toDto(time);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeMockMvc.perform(post("/api/times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRouteIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeRepository.findAll().size();
        // set the field null
        time.setRoute(null);

        // Create the Time, which fails.
        TimeDTO timeDTO = timeMapper.toDto(time);

        restTimeMockMvc.perform(post("/api/times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeDTO)))
            .andExpect(status().isBadRequest());

        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTimes() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);

        // Get all the timeList
        restTimeMockMvc.perform(get("/api/times?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(time.getId().intValue())))
            .andExpect(jsonPath("$.[*].route").value(hasItem(DEFAULT_ROUTE.toString())))
            .andExpect(jsonPath("$.[*].bus_no").value(hasItem(DEFAULT_BUS_NO.toString())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO.toString())))
            .andExpect(jsonPath("$.[*].departure").value(hasItem(DEFAULT_DEPARTURE.toString())))
            .andExpect(jsonPath("$.[*].arrival").value(hasItem(DEFAULT_ARRIVAL.toString())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())));
    }

    @Test
    @Transactional
    public void getTime() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);

        // Get the time
        restTimeMockMvc.perform(get("/api/times/{id}", time.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(time.getId().intValue()))
            .andExpect(jsonPath("$.route").value(DEFAULT_ROUTE.toString()))
            .andExpect(jsonPath("$.bus_no").value(DEFAULT_BUS_NO.toString()))
            .andExpect(jsonPath("$.from").value(DEFAULT_FROM.toString()))
            .andExpect(jsonPath("$.to").value(DEFAULT_TO.toString()))
            .andExpect(jsonPath("$.departure").value(DEFAULT_DEPARTURE.toString()))
            .andExpect(jsonPath("$.arrival").value(DEFAULT_ARRIVAL.toString()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTime() throws Exception {
        // Get the time
        restTimeMockMvc.perform(get("/api/times/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTime() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);
        timeSearchRepository.save(time);
        int databaseSizeBeforeUpdate = timeRepository.findAll().size();

        // Update the time
        Time updatedTime = timeRepository.findOne(time.getId());
        updatedTime
            .route(UPDATED_ROUTE)
            .bus_no(UPDATED_BUS_NO)
            .from(UPDATED_FROM)
            .to(UPDATED_TO)
            .departure(UPDATED_DEPARTURE)
            .arrival(UPDATED_ARRIVAL)
            .frequency(UPDATED_FREQUENCY);
        TimeDTO timeDTO = timeMapper.toDto(updatedTime);

        restTimeMockMvc.perform(put("/api/times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeDTO)))
            .andExpect(status().isOk());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeUpdate);
        Time testTime = timeList.get(timeList.size() - 1);
        assertThat(testTime.getRoute()).isEqualTo(UPDATED_ROUTE);
        assertThat(testTime.getBus_no()).isEqualTo(UPDATED_BUS_NO);
        assertThat(testTime.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testTime.getTo()).isEqualTo(UPDATED_TO);
        assertThat(testTime.getDeparture()).isEqualTo(UPDATED_DEPARTURE);
        assertThat(testTime.getArrival()).isEqualTo(UPDATED_ARRIVAL);
        assertThat(testTime.getFrequency()).isEqualTo(UPDATED_FREQUENCY);

        // Validate the Time in Elasticsearch
        Time timeEs = timeSearchRepository.findOne(testTime.getId());
        assertThat(timeEs).isEqualToComparingFieldByField(testTime);
    }

    @Test
    @Transactional
    public void updateNonExistingTime() throws Exception {
        int databaseSizeBeforeUpdate = timeRepository.findAll().size();

        // Create the Time
        TimeDTO timeDTO = timeMapper.toDto(time);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTimeMockMvc.perform(put("/api/times")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timeDTO)))
            .andExpect(status().isCreated());

        // Validate the Time in the database
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTime() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);
        timeSearchRepository.save(time);
        int databaseSizeBeforeDelete = timeRepository.findAll().size();

        // Get the time
        restTimeMockMvc.perform(delete("/api/times/{id}", time.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean timeExistsInEs = timeSearchRepository.exists(time.getId());
        assertThat(timeExistsInEs).isFalse();

        // Validate the database is empty
        List<Time> timeList = timeRepository.findAll();
        assertThat(timeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTime() throws Exception {
        // Initialize the database
        timeRepository.saveAndFlush(time);
        timeSearchRepository.save(time);

        // Search the time
        restTimeMockMvc.perform(get("/api/_search/times?query=id:" + time.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(time.getId().intValue())))
            .andExpect(jsonPath("$.[*].route").value(hasItem(DEFAULT_ROUTE.toString())))
            .andExpect(jsonPath("$.[*].bus_no").value(hasItem(DEFAULT_BUS_NO.toString())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO.toString())))
            .andExpect(jsonPath("$.[*].departure").value(hasItem(DEFAULT_DEPARTURE.toString())))
            .andExpect(jsonPath("$.[*].arrival").value(hasItem(DEFAULT_ARRIVAL.toString())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Time.class);
        Time time1 = new Time();
        time1.setId(1L);
        Time time2 = new Time();
        time2.setId(time1.getId());
        assertThat(time1).isEqualTo(time2);
        time2.setId(2L);
        assertThat(time1).isNotEqualTo(time2);
        time1.setId(null);
        assertThat(time1).isNotEqualTo(time2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeDTO.class);
        TimeDTO timeDTO1 = new TimeDTO();
        timeDTO1.setId(1L);
        TimeDTO timeDTO2 = new TimeDTO();
        assertThat(timeDTO1).isNotEqualTo(timeDTO2);
        timeDTO2.setId(timeDTO1.getId());
        assertThat(timeDTO1).isEqualTo(timeDTO2);
        timeDTO2.setId(2L);
        assertThat(timeDTO1).isNotEqualTo(timeDTO2);
        timeDTO1.setId(null);
        assertThat(timeDTO1).isNotEqualTo(timeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(timeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(timeMapper.fromId(null)).isNull();
    }
}
