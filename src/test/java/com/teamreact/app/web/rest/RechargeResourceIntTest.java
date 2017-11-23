package com.teamreact.app.web.rest;

import com.teamreact.app.TicketingSystemApp;

import com.teamreact.app.domain.Recharge;
import com.teamreact.app.repository.RechargeRepository;
import com.teamreact.app.repository.search.RechargeSearchRepository;
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
 * Test class for the RechargeResource REST controller.
 *
 * @see RechargeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingSystemApp.class)
public class RechargeResourceIntTest {

    private static final String DEFAULT_RECHARGE_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_RECHARGE_METHOD = "BBBBBBBBBB";

    private static final Integer DEFAULT_RECHARGE_AMOUNT = 1;
    private static final Integer UPDATED_RECHARGE_AMOUNT = 2;

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_EXP_DATE = "AAAAAAAAAA";
    private static final String UPDATED_EXP_DATE = "BBBBBBBBBB";

    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private RechargeSearchRepository rechargeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRechargeMockMvc;

    private Recharge recharge;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RechargeResource rechargeResource = new RechargeResource(rechargeRepository, rechargeSearchRepository);
        this.restRechargeMockMvc = MockMvcBuilders.standaloneSetup(rechargeResource)
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
    public static Recharge createEntity(EntityManager em) {
        Recharge recharge = new Recharge()
            .rechargeMethod(DEFAULT_RECHARGE_METHOD)
            .rechargeAmount(DEFAULT_RECHARGE_AMOUNT)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .cardType(DEFAULT_CARD_TYPE)
            .expDate(DEFAULT_EXP_DATE);
        return recharge;
    }

    @Before
    public void initTest() {
        rechargeSearchRepository.deleteAll();
        recharge = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecharge() throws Exception {
        int databaseSizeBeforeCreate = rechargeRepository.findAll().size();

        // Create the Recharge
        restRechargeMockMvc.perform(post("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recharge)))
            .andExpect(status().isCreated());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeCreate + 1);
        Recharge testRecharge = rechargeList.get(rechargeList.size() - 1);
        assertThat(testRecharge.getRechargeMethod()).isEqualTo(DEFAULT_RECHARGE_METHOD);
        assertThat(testRecharge.getRechargeAmount()).isEqualTo(DEFAULT_RECHARGE_AMOUNT);
        assertThat(testRecharge.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testRecharge.getCardType()).isEqualTo(DEFAULT_CARD_TYPE);
        assertThat(testRecharge.getExpDate()).isEqualTo(DEFAULT_EXP_DATE);

        // Validate the Recharge in Elasticsearch
        Recharge rechargeEs = rechargeSearchRepository.findOne(testRecharge.getId());
        assertThat(rechargeEs).isEqualToComparingFieldByField(testRecharge);
    }

    @Test
    @Transactional
    public void createRechargeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rechargeRepository.findAll().size();

        // Create the Recharge with an existing ID
        recharge.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRechargeMockMvc.perform(post("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recharge)))
            .andExpect(status().isBadRequest());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRecharges() throws Exception {
        // Initialize the database
        rechargeRepository.saveAndFlush(recharge);

        // Get all the rechargeList
        restRechargeMockMvc.perform(get("/api/recharges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].rechargeMethod").value(hasItem(DEFAULT_RECHARGE_METHOD.toString())))
            .andExpect(jsonPath("$.[*].rechargeAmount").value(hasItem(DEFAULT_RECHARGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expDate").value(hasItem(DEFAULT_EXP_DATE.toString())));
    }

    @Test
    @Transactional
    public void getRecharge() throws Exception {
        // Initialize the database
        rechargeRepository.saveAndFlush(recharge);

        // Get the recharge
        restRechargeMockMvc.perform(get("/api/recharges/{id}", recharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recharge.getId().intValue()))
            .andExpect(jsonPath("$.rechargeMethod").value(DEFAULT_RECHARGE_METHOD.toString()))
            .andExpect(jsonPath("$.rechargeAmount").value(DEFAULT_RECHARGE_AMOUNT))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER.toString()))
            .andExpect(jsonPath("$.cardType").value(DEFAULT_CARD_TYPE.toString()))
            .andExpect(jsonPath("$.expDate").value(DEFAULT_EXP_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRecharge() throws Exception {
        // Get the recharge
        restRechargeMockMvc.perform(get("/api/recharges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecharge() throws Exception {
        // Initialize the database
        rechargeRepository.saveAndFlush(recharge);
        rechargeSearchRepository.save(recharge);
        int databaseSizeBeforeUpdate = rechargeRepository.findAll().size();

        // Update the recharge
        Recharge updatedRecharge = rechargeRepository.findOne(recharge.getId());
        updatedRecharge
            .rechargeMethod(UPDATED_RECHARGE_METHOD)
            .rechargeAmount(UPDATED_RECHARGE_AMOUNT)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .cardType(UPDATED_CARD_TYPE)
            .expDate(UPDATED_EXP_DATE);

        restRechargeMockMvc.perform(put("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecharge)))
            .andExpect(status().isOk());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeUpdate);
        Recharge testRecharge = rechargeList.get(rechargeList.size() - 1);
        assertThat(testRecharge.getRechargeMethod()).isEqualTo(UPDATED_RECHARGE_METHOD);
        assertThat(testRecharge.getRechargeAmount()).isEqualTo(UPDATED_RECHARGE_AMOUNT);
        assertThat(testRecharge.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testRecharge.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testRecharge.getExpDate()).isEqualTo(UPDATED_EXP_DATE);

        // Validate the Recharge in Elasticsearch
        Recharge rechargeEs = rechargeSearchRepository.findOne(testRecharge.getId());
        assertThat(rechargeEs).isEqualToComparingFieldByField(testRecharge);
    }

    @Test
    @Transactional
    public void updateNonExistingRecharge() throws Exception {
        int databaseSizeBeforeUpdate = rechargeRepository.findAll().size();

        // Create the Recharge

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRechargeMockMvc.perform(put("/api/recharges")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recharge)))
            .andExpect(status().isCreated());

        // Validate the Recharge in the database
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRecharge() throws Exception {
        // Initialize the database
        rechargeRepository.saveAndFlush(recharge);
        rechargeSearchRepository.save(recharge);
        int databaseSizeBeforeDelete = rechargeRepository.findAll().size();

        // Get the recharge
        restRechargeMockMvc.perform(delete("/api/recharges/{id}", recharge.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean rechargeExistsInEs = rechargeSearchRepository.exists(recharge.getId());
        assertThat(rechargeExistsInEs).isFalse();

        // Validate the database is empty
        List<Recharge> rechargeList = rechargeRepository.findAll();
        assertThat(rechargeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRecharge() throws Exception {
        // Initialize the database
        rechargeRepository.saveAndFlush(recharge);
        rechargeSearchRepository.save(recharge);

        // Search the recharge
        restRechargeMockMvc.perform(get("/api/_search/recharges?query=id:" + recharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].rechargeMethod").value(hasItem(DEFAULT_RECHARGE_METHOD.toString())))
            .andExpect(jsonPath("$.[*].rechargeAmount").value(hasItem(DEFAULT_RECHARGE_AMOUNT)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expDate").value(hasItem(DEFAULT_EXP_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recharge.class);
        Recharge recharge1 = new Recharge();
        recharge1.setId(1L);
        Recharge recharge2 = new Recharge();
        recharge2.setId(recharge1.getId());
        assertThat(recharge1).isEqualTo(recharge2);
        recharge2.setId(2L);
        assertThat(recharge1).isNotEqualTo(recharge2);
        recharge1.setId(null);
        assertThat(recharge1).isNotEqualTo(recharge2);
    }
}
