package com.teamreact.app.web.rest;

import com.teamreact.app.TicketingSystemApp;

import com.teamreact.app.domain.TransportManager;
import com.teamreact.app.domain.TimeTable;
import com.teamreact.app.repository.TransportManagerRepository;
import com.teamreact.app.repository.search.TransportManagerSearchRepository;
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
 * Test class for the TransportManagerResource REST controller.
 *
 * @see TransportManagerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingSystemApp.class)
public class TransportManagerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NIC = "AAAAAAAAAA";
    private static final String UPDATED_NIC = "BBBBBBBBBB";

    @Autowired
    private TransportManagerRepository transportManagerRepository;

    @Autowired
    private TransportManagerSearchRepository transportManagerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransportManagerMockMvc;

    private TransportManager transportManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransportManagerResource transportManagerResource = new TransportManagerResource(transportManagerRepository, transportManagerSearchRepository);
        this.restTransportManagerMockMvc = MockMvcBuilders.standaloneSetup(transportManagerResource)
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
    public static TransportManager createEntity(EntityManager em) {
        TransportManager transportManager = new TransportManager()
            .name(DEFAULT_NAME)
            .nic(DEFAULT_NIC);
        // Add required entity
        TimeTable timeTable = TimeTableResourceIntTest.createEntity(em);
        em.persist(timeTable);
        em.flush();
        transportManager.getTimeTables().add(timeTable);
        return transportManager;
    }

    @Before
    public void initTest() {
        transportManagerSearchRepository.deleteAll();
        transportManager = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransportManager() throws Exception {
        int databaseSizeBeforeCreate = transportManagerRepository.findAll().size();

        // Create the TransportManager
        restTransportManagerMockMvc.perform(post("/api/transport-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transportManager)))
            .andExpect(status().isCreated());

        // Validate the TransportManager in the database
        List<TransportManager> transportManagerList = transportManagerRepository.findAll();
        assertThat(transportManagerList).hasSize(databaseSizeBeforeCreate + 1);
        TransportManager testTransportManager = transportManagerList.get(transportManagerList.size() - 1);
        assertThat(testTransportManager.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTransportManager.getNic()).isEqualTo(DEFAULT_NIC);

        // Validate the TransportManager in Elasticsearch
        TransportManager transportManagerEs = transportManagerSearchRepository.findOne(testTransportManager.getId());
        assertThat(transportManagerEs).isEqualToComparingFieldByField(testTransportManager);
    }

    @Test
    @Transactional
    public void createTransportManagerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transportManagerRepository.findAll().size();

        // Create the TransportManager with an existing ID
        transportManager.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransportManagerMockMvc.perform(post("/api/transport-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transportManager)))
            .andExpect(status().isBadRequest());

        // Validate the TransportManager in the database
        List<TransportManager> transportManagerList = transportManagerRepository.findAll();
        assertThat(transportManagerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTransportManagers() throws Exception {
        // Initialize the database
        transportManagerRepository.saveAndFlush(transportManager);

        // Get all the transportManagerList
        restTransportManagerMockMvc.perform(get("/api/transport-managers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transportManager.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].nic").value(hasItem(DEFAULT_NIC.toString())));
    }

    @Test
    @Transactional
    public void getTransportManager() throws Exception {
        // Initialize the database
        transportManagerRepository.saveAndFlush(transportManager);

        // Get the transportManager
        restTransportManagerMockMvc.perform(get("/api/transport-managers/{id}", transportManager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transportManager.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.nic").value(DEFAULT_NIC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransportManager() throws Exception {
        // Get the transportManager
        restTransportManagerMockMvc.perform(get("/api/transport-managers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransportManager() throws Exception {
        // Initialize the database
        transportManagerRepository.saveAndFlush(transportManager);
        transportManagerSearchRepository.save(transportManager);
        int databaseSizeBeforeUpdate = transportManagerRepository.findAll().size();

        // Update the transportManager
        TransportManager updatedTransportManager = transportManagerRepository.findOne(transportManager.getId());
        updatedTransportManager
            .name(UPDATED_NAME)
            .nic(UPDATED_NIC);

        restTransportManagerMockMvc.perform(put("/api/transport-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransportManager)))
            .andExpect(status().isOk());

        // Validate the TransportManager in the database
        List<TransportManager> transportManagerList = transportManagerRepository.findAll();
        assertThat(transportManagerList).hasSize(databaseSizeBeforeUpdate);
        TransportManager testTransportManager = transportManagerList.get(transportManagerList.size() - 1);
        assertThat(testTransportManager.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTransportManager.getNic()).isEqualTo(UPDATED_NIC);

        // Validate the TransportManager in Elasticsearch
        TransportManager transportManagerEs = transportManagerSearchRepository.findOne(testTransportManager.getId());
        assertThat(transportManagerEs).isEqualToComparingFieldByField(testTransportManager);
    }

    @Test
    @Transactional
    public void updateNonExistingTransportManager() throws Exception {
        int databaseSizeBeforeUpdate = transportManagerRepository.findAll().size();

        // Create the TransportManager

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransportManagerMockMvc.perform(put("/api/transport-managers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transportManager)))
            .andExpect(status().isCreated());

        // Validate the TransportManager in the database
        List<TransportManager> transportManagerList = transportManagerRepository.findAll();
        assertThat(transportManagerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransportManager() throws Exception {
        // Initialize the database
        transportManagerRepository.saveAndFlush(transportManager);
        transportManagerSearchRepository.save(transportManager);
        int databaseSizeBeforeDelete = transportManagerRepository.findAll().size();

        // Get the transportManager
        restTransportManagerMockMvc.perform(delete("/api/transport-managers/{id}", transportManager.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean transportManagerExistsInEs = transportManagerSearchRepository.exists(transportManager.getId());
        assertThat(transportManagerExistsInEs).isFalse();

        // Validate the database is empty
        List<TransportManager> transportManagerList = transportManagerRepository.findAll();
        assertThat(transportManagerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransportManager() throws Exception {
        // Initialize the database
        transportManagerRepository.saveAndFlush(transportManager);
        transportManagerSearchRepository.save(transportManager);

        // Search the transportManager
        restTransportManagerMockMvc.perform(get("/api/_search/transport-managers?query=id:" + transportManager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transportManager.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].nic").value(hasItem(DEFAULT_NIC.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransportManager.class);
        TransportManager transportManager1 = new TransportManager();
        transportManager1.setId(1L);
        TransportManager transportManager2 = new TransportManager();
        transportManager2.setId(transportManager1.getId());
        assertThat(transportManager1).isEqualTo(transportManager2);
        transportManager2.setId(2L);
        assertThat(transportManager1).isNotEqualTo(transportManager2);
        transportManager1.setId(null);
        assertThat(transportManager1).isNotEqualTo(transportManager2);
    }
}
