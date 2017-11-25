package com.teamreact.app.web.rest;

import com.teamreact.app.TicketingSystemApp;

import com.teamreact.app.domain.Reports;
import com.teamreact.app.repository.ReportsRepository;
import com.teamreact.app.service.ReportsService;
import com.teamreact.app.repository.search.ReportsSearchRepository;
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
 * Test class for the ReportsResource REST controller.
 *
 * @see ReportsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingSystemApp.class)
public class ReportsResourceIntTest {

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REPORT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_TYPE = "BBBBBBBBBB";

    @Autowired
    private ReportsRepository reportsRepository;

    @Autowired
    private ReportsService reportsService;

    @Autowired
    private ReportsSearchRepository reportsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReportsMockMvc;

    private Reports reports;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReportsResource reportsResource = new ReportsResource(reportsService);
        this.restReportsMockMvc = MockMvcBuilders.standaloneSetup(reportsResource)
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
    public static Reports createEntity(EntityManager em) {
        Reports reports = new Reports()
            .reportName(DEFAULT_REPORT_NAME)
            .reportType(DEFAULT_REPORT_TYPE);
        return reports;
    }

    @Before
    public void initTest() {
        reportsSearchRepository.deleteAll();
        reports = createEntity(em);
    }

    @Test
    @Transactional
    public void createReports() throws Exception {
        int databaseSizeBeforeCreate = reportsRepository.findAll().size();

        // Create the Reports
        restReportsMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reports)))
            .andExpect(status().isCreated());

        // Validate the Reports in the database
        List<Reports> reportsList = reportsRepository.findAll();
        assertThat(reportsList).hasSize(databaseSizeBeforeCreate + 1);
        Reports testReports = reportsList.get(reportsList.size() - 1);
        assertThat(testReports.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testReports.getReportType()).isEqualTo(DEFAULT_REPORT_TYPE);

        // Validate the Reports in Elasticsearch
        Reports reportsEs = reportsSearchRepository.findOne(testReports.getId());
        assertThat(reportsEs).isEqualToComparingFieldByField(testReports);
    }

    @Test
    @Transactional
    public void createReportsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reportsRepository.findAll().size();

        // Create the Reports with an existing ID
        reports.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportsMockMvc.perform(post("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reports)))
            .andExpect(status().isBadRequest());

        // Validate the Reports in the database
        List<Reports> reportsList = reportsRepository.findAll();
        assertThat(reportsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReports() throws Exception {
        // Initialize the database
        reportsRepository.saveAndFlush(reports);

        // Get all the reportsList
        restReportsMockMvc.perform(get("/api/reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reports.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME.toString())))
            .andExpect(jsonPath("$.[*].reportType").value(hasItem(DEFAULT_REPORT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getReports() throws Exception {
        // Initialize the database
        reportsRepository.saveAndFlush(reports);

        // Get the reports
        restReportsMockMvc.perform(get("/api/reports/{id}", reports.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reports.getId().intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME.toString()))
            .andExpect(jsonPath("$.reportType").value(DEFAULT_REPORT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReports() throws Exception {
        // Get the reports
        restReportsMockMvc.perform(get("/api/reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReports() throws Exception {
        // Initialize the database
        reportsService.save(reports);

        int databaseSizeBeforeUpdate = reportsRepository.findAll().size();

        // Update the reports
        Reports updatedReports = reportsRepository.findOne(reports.getId());
        updatedReports
            .reportName(UPDATED_REPORT_NAME)
            .reportType(UPDATED_REPORT_TYPE);

        restReportsMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReports)))
            .andExpect(status().isOk());

        // Validate the Reports in the database
        List<Reports> reportsList = reportsRepository.findAll();
        assertThat(reportsList).hasSize(databaseSizeBeforeUpdate);
        Reports testReports = reportsList.get(reportsList.size() - 1);
        assertThat(testReports.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testReports.getReportType()).isEqualTo(UPDATED_REPORT_TYPE);

        // Validate the Reports in Elasticsearch
        Reports reportsEs = reportsSearchRepository.findOne(testReports.getId());
        assertThat(reportsEs).isEqualToComparingFieldByField(testReports);
    }

    @Test
    @Transactional
    public void updateNonExistingReports() throws Exception {
        int databaseSizeBeforeUpdate = reportsRepository.findAll().size();

        // Create the Reports

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReportsMockMvc.perform(put("/api/reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reports)))
            .andExpect(status().isCreated());

        // Validate the Reports in the database
        List<Reports> reportsList = reportsRepository.findAll();
        assertThat(reportsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReports() throws Exception {
        // Initialize the database
        reportsService.save(reports);

        int databaseSizeBeforeDelete = reportsRepository.findAll().size();

        // Get the reports
        restReportsMockMvc.perform(delete("/api/reports/{id}", reports.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean reportsExistsInEs = reportsSearchRepository.exists(reports.getId());
        assertThat(reportsExistsInEs).isFalse();

        // Validate the database is empty
        List<Reports> reportsList = reportsRepository.findAll();
        assertThat(reportsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReports() throws Exception {
        // Initialize the database
        reportsService.save(reports);

        // Search the reports
        restReportsMockMvc.perform(get("/api/_search/reports?query=id:" + reports.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reports.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME.toString())))
            .andExpect(jsonPath("$.[*].reportType").value(hasItem(DEFAULT_REPORT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reports.class);
        Reports reports1 = new Reports();
        reports1.setId(1L);
        Reports reports2 = new Reports();
        reports2.setId(reports1.getId());
        assertThat(reports1).isEqualTo(reports2);
        reports2.setId(2L);
        assertThat(reports1).isNotEqualTo(reports2);
        reports1.setId(null);
        assertThat(reports1).isNotEqualTo(reports2);
    }
}
