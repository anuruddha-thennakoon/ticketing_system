package com.teamreact.app.web.rest;

import com.teamreact.app.TicketingSystemApp;

import com.teamreact.app.domain.SmartCard;
import com.teamreact.app.domain.Journey;
import com.teamreact.app.domain.Payment;
import com.teamreact.app.domain.Recharge;
import com.teamreact.app.repository.SmartCardRepository;
import com.teamreact.app.repository.search.SmartCardSearchRepository;
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
 * Test class for the SmartCardResource REST controller.
 *
 * @see SmartCardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketingSystemApp.class)
public class SmartCardResourceIntTest {

    private static final Integer DEFAULT_CARD_BALANCE = 1;
    private static final Integer UPDATED_CARD_BALANCE = 2;

    private static final String DEFAULT_ISSUED_DATE = "AAAAAAAAAA";
    private static final String UPDATED_ISSUED_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NUMBER = "BBBBBBBBBB";

    @Autowired
    private SmartCardRepository smartCardRepository;

    @Autowired
    private SmartCardSearchRepository smartCardSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSmartCardMockMvc;

    private SmartCard smartCard;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmartCardResource smartCardResource = new SmartCardResource(smartCardRepository, smartCardSearchRepository);
        this.restSmartCardMockMvc = MockMvcBuilders.standaloneSetup(smartCardResource)
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
    public static SmartCard createEntity(EntityManager em) {
        SmartCard smartCard = new SmartCard()
            .cardBalance(DEFAULT_CARD_BALANCE)
            .issuedDate(DEFAULT_ISSUED_DATE)
            .cardNumber(DEFAULT_CARD_NUMBER);
        // Add required entity
        Journey journey = JourneyResourceIntTest.createEntity(em);
        em.persist(journey);
        em.flush();
        smartCard.getJourneys().add(journey);
        // Add required entity
        Payment payment = PaymentResourceIntTest.createEntity(em);
        em.persist(payment);
        em.flush();
        smartCard.getPayments().add(payment);
        // Add required entity
        Recharge recharge = RechargeResourceIntTest.createEntity(em);
        em.persist(recharge);
        em.flush();
        smartCard.getRecharges().add(recharge);
        return smartCard;
    }

    @Before
    public void initTest() {
        smartCardSearchRepository.deleteAll();
        smartCard = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmartCard() throws Exception {
        int databaseSizeBeforeCreate = smartCardRepository.findAll().size();

        // Create the SmartCard
        restSmartCardMockMvc.perform(post("/api/smart-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartCard)))
            .andExpect(status().isCreated());

        // Validate the SmartCard in the database
        List<SmartCard> smartCardList = smartCardRepository.findAll();
        assertThat(smartCardList).hasSize(databaseSizeBeforeCreate + 1);
        SmartCard testSmartCard = smartCardList.get(smartCardList.size() - 1);
        assertThat(testSmartCard.getCardBalance()).isEqualTo(DEFAULT_CARD_BALANCE);
        assertThat(testSmartCard.getIssuedDate()).isEqualTo(DEFAULT_ISSUED_DATE);
        assertThat(testSmartCard.getCardNumber()).isEqualTo(DEFAULT_CARD_NUMBER);

        // Validate the SmartCard in Elasticsearch
        SmartCard smartCardEs = smartCardSearchRepository.findOne(testSmartCard.getId());
        assertThat(smartCardEs).isEqualToComparingFieldByField(testSmartCard);
    }

    @Test
    @Transactional
    public void createSmartCardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smartCardRepository.findAll().size();

        // Create the SmartCard with an existing ID
        smartCard.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmartCardMockMvc.perform(post("/api/smart-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartCard)))
            .andExpect(status().isBadRequest());

        // Validate the SmartCard in the database
        List<SmartCard> smartCardList = smartCardRepository.findAll();
        assertThat(smartCardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSmartCards() throws Exception {
        // Initialize the database
        smartCardRepository.saveAndFlush(smartCard);

        // Get all the smartCardList
        restSmartCardMockMvc.perform(get("/api/smart-cards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smartCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardBalance").value(hasItem(DEFAULT_CARD_BALANCE)))
            .andExpect(jsonPath("$.[*].issuedDate").value(hasItem(DEFAULT_ISSUED_DATE.toString())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getSmartCard() throws Exception {
        // Initialize the database
        smartCardRepository.saveAndFlush(smartCard);

        // Get the smartCard
        restSmartCardMockMvc.perform(get("/api/smart-cards/{id}", smartCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smartCard.getId().intValue()))
            .andExpect(jsonPath("$.cardBalance").value(DEFAULT_CARD_BALANCE))
            .andExpect(jsonPath("$.issuedDate").value(DEFAULT_ISSUED_DATE.toString()))
            .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSmartCard() throws Exception {
        // Get the smartCard
        restSmartCardMockMvc.perform(get("/api/smart-cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmartCard() throws Exception {
        // Initialize the database
        smartCardRepository.saveAndFlush(smartCard);
        smartCardSearchRepository.save(smartCard);
        int databaseSizeBeforeUpdate = smartCardRepository.findAll().size();

        // Update the smartCard
        SmartCard updatedSmartCard = smartCardRepository.findOne(smartCard.getId());
        updatedSmartCard
            .cardBalance(UPDATED_CARD_BALANCE)
            .issuedDate(UPDATED_ISSUED_DATE)
            .cardNumber(UPDATED_CARD_NUMBER);

        restSmartCardMockMvc.perform(put("/api/smart-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSmartCard)))
            .andExpect(status().isOk());

        // Validate the SmartCard in the database
        List<SmartCard> smartCardList = smartCardRepository.findAll();
        assertThat(smartCardList).hasSize(databaseSizeBeforeUpdate);
        SmartCard testSmartCard = smartCardList.get(smartCardList.size() - 1);
        assertThat(testSmartCard.getCardBalance()).isEqualTo(UPDATED_CARD_BALANCE);
        assertThat(testSmartCard.getIssuedDate()).isEqualTo(UPDATED_ISSUED_DATE);
        assertThat(testSmartCard.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);

        // Validate the SmartCard in Elasticsearch
        SmartCard smartCardEs = smartCardSearchRepository.findOne(testSmartCard.getId());
        assertThat(smartCardEs).isEqualToComparingFieldByField(testSmartCard);
    }

    @Test
    @Transactional
    public void updateNonExistingSmartCard() throws Exception {
        int databaseSizeBeforeUpdate = smartCardRepository.findAll().size();

        // Create the SmartCard

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSmartCardMockMvc.perform(put("/api/smart-cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartCard)))
            .andExpect(status().isCreated());

        // Validate the SmartCard in the database
        List<SmartCard> smartCardList = smartCardRepository.findAll();
        assertThat(smartCardList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSmartCard() throws Exception {
        // Initialize the database
        smartCardRepository.saveAndFlush(smartCard);
        smartCardSearchRepository.save(smartCard);
        int databaseSizeBeforeDelete = smartCardRepository.findAll().size();

        // Get the smartCard
        restSmartCardMockMvc.perform(delete("/api/smart-cards/{id}", smartCard.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean smartCardExistsInEs = smartCardSearchRepository.exists(smartCard.getId());
        assertThat(smartCardExistsInEs).isFalse();

        // Validate the database is empty
        List<SmartCard> smartCardList = smartCardRepository.findAll();
        assertThat(smartCardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSmartCard() throws Exception {
        // Initialize the database
        smartCardRepository.saveAndFlush(smartCard);
        smartCardSearchRepository.save(smartCard);

        // Search the smartCard
        restSmartCardMockMvc.perform(get("/api/_search/smart-cards?query=id:" + smartCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smartCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardBalance").value(hasItem(DEFAULT_CARD_BALANCE)))
            .andExpect(jsonPath("$.[*].issuedDate").value(hasItem(DEFAULT_ISSUED_DATE.toString())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmartCard.class);
        SmartCard smartCard1 = new SmartCard();
        smartCard1.setId(1L);
        SmartCard smartCard2 = new SmartCard();
        smartCard2.setId(smartCard1.getId());
        assertThat(smartCard1).isEqualTo(smartCard2);
        smartCard2.setId(2L);
        assertThat(smartCard1).isNotEqualTo(smartCard2);
        smartCard1.setId(null);
        assertThat(smartCard1).isNotEqualTo(smartCard2);
    }
}
