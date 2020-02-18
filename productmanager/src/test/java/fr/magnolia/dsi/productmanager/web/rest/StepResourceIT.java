package fr.magnolia.dsi.productmanager.web.rest;

import fr.magnolia.dsi.productmanager.ProductmanagerApp;
import fr.magnolia.dsi.productmanager.domain.Step;
import fr.magnolia.dsi.productmanager.repository.StepRepository;
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
 * Integration tests for the {@link StepResource} REST controller.
 */
@SpringBootTest(classes = ProductmanagerApp.class)
public class StepResourceIT {

    private static final Long DEFAULT_STEP_ID = 1L;
    private static final Long UPDATED_STEP_ID = 2L;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restStepMockMvc;

    private Step step;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StepResource stepResource = new StepResource(stepRepository);
        this.restStepMockMvc = MockMvcBuilders.standaloneSetup(stepResource)
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
    public static Step createEntity() {
        Step step = new Step()
            .stepId(DEFAULT_STEP_ID)
            .code(DEFAULT_CODE)
            .label(DEFAULT_LABEL);
        return step;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Step createUpdatedEntity() {
        Step step = new Step()
            .stepId(UPDATED_STEP_ID)
            .code(UPDATED_CODE)
            .label(UPDATED_LABEL);
        return step;
    }

    @BeforeEach
    public void initTest() {
        stepRepository.deleteAll();
        step = createEntity();
    }

    @Test
    public void createStep() throws Exception {
        int databaseSizeBeforeCreate = stepRepository.findAll().size();

        // Create the Step
        restStepMockMvc.perform(post("/api/steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(step)))
            .andExpect(status().isCreated());

        // Validate the Step in the database
        List<Step> stepList = stepRepository.findAll();
        assertThat(stepList).hasSize(databaseSizeBeforeCreate + 1);
        Step testStep = stepList.get(stepList.size() - 1);
        assertThat(testStep.getStepId()).isEqualTo(DEFAULT_STEP_ID);
        assertThat(testStep.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testStep.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    public void createStepWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stepRepository.findAll().size();

        // Create the Step with an existing ID
        step.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restStepMockMvc.perform(post("/api/steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(step)))
            .andExpect(status().isBadRequest());

        // Validate the Step in the database
        List<Step> stepList = stepRepository.findAll();
        assertThat(stepList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkStepIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = stepRepository.findAll().size();
        // set the field null
        step.setStepId(null);

        // Create the Step, which fails.

        restStepMockMvc.perform(post("/api/steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(step)))
            .andExpect(status().isBadRequest());

        List<Step> stepList = stepRepository.findAll();
        assertThat(stepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stepRepository.findAll().size();
        // set the field null
        step.setCode(null);

        // Create the Step, which fails.

        restStepMockMvc.perform(post("/api/steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(step)))
            .andExpect(status().isBadRequest());

        List<Step> stepList = stepRepository.findAll();
        assertThat(stepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = stepRepository.findAll().size();
        // set the field null
        step.setLabel(null);

        // Create the Step, which fails.

        restStepMockMvc.perform(post("/api/steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(step)))
            .andExpect(status().isBadRequest());

        List<Step> stepList = stepRepository.findAll();
        assertThat(stepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSteps() throws Exception {
        // Initialize the database
        stepRepository.save(step);

        // Get all the stepList
        restStepMockMvc.perform(get("/api/steps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(step.getId())))
            .andExpect(jsonPath("$.[*].stepId").value(hasItem(DEFAULT_STEP_ID.intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }
    
    @Test
    public void getStep() throws Exception {
        // Initialize the database
        stepRepository.save(step);

        // Get the step
        restStepMockMvc.perform(get("/api/steps/{id}", step.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(step.getId()))
            .andExpect(jsonPath("$.stepId").value(DEFAULT_STEP_ID.intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }

    @Test
    public void getNonExistingStep() throws Exception {
        // Get the step
        restStepMockMvc.perform(get("/api/steps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateStep() throws Exception {
        // Initialize the database
        stepRepository.save(step);

        int databaseSizeBeforeUpdate = stepRepository.findAll().size();

        // Update the step
        Step updatedStep = stepRepository.findById(step.getId()).get();
        updatedStep
            .stepId(UPDATED_STEP_ID)
            .code(UPDATED_CODE)
            .label(UPDATED_LABEL);

        restStepMockMvc.perform(put("/api/steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStep)))
            .andExpect(status().isOk());

        // Validate the Step in the database
        List<Step> stepList = stepRepository.findAll();
        assertThat(stepList).hasSize(databaseSizeBeforeUpdate);
        Step testStep = stepList.get(stepList.size() - 1);
        assertThat(testStep.getStepId()).isEqualTo(UPDATED_STEP_ID);
        assertThat(testStep.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStep.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    public void updateNonExistingStep() throws Exception {
        int databaseSizeBeforeUpdate = stepRepository.findAll().size();

        // Create the Step

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStepMockMvc.perform(put("/api/steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(step)))
            .andExpect(status().isBadRequest());

        // Validate the Step in the database
        List<Step> stepList = stepRepository.findAll();
        assertThat(stepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteStep() throws Exception {
        // Initialize the database
        stepRepository.save(step);

        int databaseSizeBeforeDelete = stepRepository.findAll().size();

        // Delete the step
        restStepMockMvc.perform(delete("/api/steps/{id}", step.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Step> stepList = stepRepository.findAll();
        assertThat(stepList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
