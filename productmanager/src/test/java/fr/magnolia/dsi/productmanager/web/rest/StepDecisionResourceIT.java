package fr.magnolia.dsi.productmanager.web.rest;

import fr.magnolia.dsi.productmanager.ProductmanagerApp;
import fr.magnolia.dsi.productmanager.domain.StepDecision;
import fr.magnolia.dsi.productmanager.repository.StepDecisionRepository;
import fr.magnolia.dsi.productmanager.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.List;

import static fr.magnolia.dsi.productmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StepDecisionResource} REST controller.
 */
@SpringBootTest(classes = ProductmanagerApp.class)
public class StepDecisionResourceIT {

    private static final Long DEFAULT_STEP_DECISION_ID = 1L;
    private static final Long UPDATED_STEP_DECISION_ID = 2L;

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final Boolean DEFAULT_FINAL_STEP = false;
    private static final Boolean UPDATED_FINAL_STEP = true;

    @Autowired
    private StepDecisionRepository stepDecisionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restStepDecisionMockMvc;

    private StepDecision stepDecision;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StepDecisionResource stepDecisionResource = new StepDecisionResource(stepDecisionRepository);
        this.restStepDecisionMockMvc = MockMvcBuilders.standaloneSetup(stepDecisionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StepDecision createEntity() {
        StepDecision stepDecision = new StepDecision()
            .stepDecisionId(DEFAULT_STEP_DECISION_ID)
            .order(DEFAULT_ORDER)
            .finalStep(DEFAULT_FINAL_STEP);
        return stepDecision;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StepDecision createUpdatedEntity() {
        StepDecision stepDecision = new StepDecision()
            .stepDecisionId(UPDATED_STEP_DECISION_ID)
            .order(UPDATED_ORDER)
            .finalStep(UPDATED_FINAL_STEP);
        return stepDecision;
    }

    @BeforeEach
    public void initTest() {
        stepDecisionRepository.deleteAll();
        stepDecision = createEntity();
    }

    @Test
    public void createStepDecision() throws Exception {
        int databaseSizeBeforeCreate = stepDecisionRepository.findAll().size();

        // Create the StepDecision
        restStepDecisionMockMvc.perform(post("/api/step-decisions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stepDecision)))
            .andExpect(status().isCreated());

        // Validate the StepDecision in the database
        List<StepDecision> stepDecisionList = stepDecisionRepository.findAll();
        assertThat(stepDecisionList).hasSize(databaseSizeBeforeCreate + 1);
        StepDecision testStepDecision = stepDecisionList.get(stepDecisionList.size() - 1);
        assertThat(testStepDecision.getStepDecisionId()).isEqualTo(DEFAULT_STEP_DECISION_ID);
        assertThat(testStepDecision.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testStepDecision.isFinalStep()).isEqualTo(DEFAULT_FINAL_STEP);
    }

    @Test
    public void createStepDecisionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stepDecisionRepository.findAll().size();

        // Create the StepDecision with an existing ID
        stepDecision.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restStepDecisionMockMvc.perform(post("/api/step-decisions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stepDecision)))
            .andExpect(status().isBadRequest());

        // Validate the StepDecision in the database
        List<StepDecision> stepDecisionList = stepDecisionRepository.findAll();
        assertThat(stepDecisionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkStepDecisionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = stepDecisionRepository.findAll().size();
        // set the field null
        stepDecision.setStepDecisionId(null);

        // Create the StepDecision, which fails.

        restStepDecisionMockMvc.perform(post("/api/step-decisions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stepDecision)))
            .andExpect(status().isBadRequest());

        List<StepDecision> stepDecisionList = stepDecisionRepository.findAll();
        assertThat(stepDecisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = stepDecisionRepository.findAll().size();
        // set the field null
        stepDecision.setOrder(null);

        // Create the StepDecision, which fails.

        restStepDecisionMockMvc.perform(post("/api/step-decisions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stepDecision)))
            .andExpect(status().isBadRequest());

        List<StepDecision> stepDecisionList = stepDecisionRepository.findAll();
        assertThat(stepDecisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllStepDecisions() throws Exception {
        // Initialize the database
        stepDecisionRepository.save(stepDecision);

        // Get all the stepDecisionList
        restStepDecisionMockMvc.perform(get("/api/step-decisions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stepDecision.getId())))
            .andExpect(jsonPath("$.[*].stepDecisionId").value(hasItem(DEFAULT_STEP_DECISION_ID.intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].finalStep").value(hasItem(DEFAULT_FINAL_STEP.booleanValue())));
    }
    
    @Test
    public void getStepDecision() throws Exception {
        // Initialize the database
        stepDecisionRepository.save(stepDecision);

        // Get the stepDecision
        restStepDecisionMockMvc.perform(get("/api/step-decisions/{id}", stepDecision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stepDecision.getId()))
            .andExpect(jsonPath("$.stepDecisionId").value(DEFAULT_STEP_DECISION_ID.intValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.finalStep").value(DEFAULT_FINAL_STEP.booleanValue()));
    }

    @Test
    public void getNonExistingStepDecision() throws Exception {
        // Get the stepDecision
        restStepDecisionMockMvc.perform(get("/api/step-decisions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateStepDecision() throws Exception {
        // Initialize the database
        stepDecisionRepository.save(stepDecision);

        int databaseSizeBeforeUpdate = stepDecisionRepository.findAll().size();

        // Update the stepDecision
        StepDecision updatedStepDecision = stepDecisionRepository.findById(stepDecision.getId()).get();
        updatedStepDecision
            .stepDecisionId(UPDATED_STEP_DECISION_ID)
            .order(UPDATED_ORDER)
            .finalStep(UPDATED_FINAL_STEP);

        restStepDecisionMockMvc.perform(put("/api/step-decisions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStepDecision)))
            .andExpect(status().isOk());

        // Validate the StepDecision in the database
        List<StepDecision> stepDecisionList = stepDecisionRepository.findAll();
        assertThat(stepDecisionList).hasSize(databaseSizeBeforeUpdate);
        StepDecision testStepDecision = stepDecisionList.get(stepDecisionList.size() - 1);
        assertThat(testStepDecision.getStepDecisionId()).isEqualTo(UPDATED_STEP_DECISION_ID);
        assertThat(testStepDecision.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testStepDecision.isFinalStep()).isEqualTo(UPDATED_FINAL_STEP);
    }

    @Test
    public void updateNonExistingStepDecision() throws Exception {
        int databaseSizeBeforeUpdate = stepDecisionRepository.findAll().size();

        // Create the StepDecision

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStepDecisionMockMvc.perform(put("/api/step-decisions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stepDecision)))
            .andExpect(status().isBadRequest());

        // Validate the StepDecision in the database
        List<StepDecision> stepDecisionList = stepDecisionRepository.findAll();
        assertThat(stepDecisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteStepDecision() throws Exception {
        // Initialize the database
        stepDecisionRepository.save(stepDecision);

        int databaseSizeBeforeDelete = stepDecisionRepository.findAll().size();

        // Delete the stepDecision
        restStepDecisionMockMvc.perform(delete("/api/step-decisions/{id}", stepDecision.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StepDecision> stepDecisionList = stepDecisionRepository.findAll();
        assertThat(stepDecisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
