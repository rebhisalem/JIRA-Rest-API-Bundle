package fr.magnolia.dsi.productmanager.web.rest;

import fr.magnolia.dsi.productmanager.ProductmanagerApp;
import fr.magnolia.dsi.productmanager.domain.SurveyQuestion;
import fr.magnolia.dsi.productmanager.repository.SurveyQuestionRepository;
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
 * Integration tests for the {@link SurveyQuestionResource} REST controller.
 */
@SpringBootTest(classes = ProductmanagerApp.class)
public class SurveyQuestionResourceIT {

    private static final Long DEFAULT_SURVEY_QUESTION_ID = 1L;
    private static final Long UPDATED_SURVEY_QUESTION_ID = 2L;

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restSurveyQuestionMockMvc;

    private SurveyQuestion surveyQuestion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SurveyQuestionResource surveyQuestionResource = new SurveyQuestionResource(surveyQuestionRepository);
        this.restSurveyQuestionMockMvc = MockMvcBuilders.standaloneSetup(surveyQuestionResource)
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
    public static SurveyQuestion createEntity() {
        SurveyQuestion surveyQuestion = new SurveyQuestion()
            .surveyQuestionId(DEFAULT_SURVEY_QUESTION_ID)
            .order(DEFAULT_ORDER);
        return surveyQuestion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SurveyQuestion createUpdatedEntity() {
        SurveyQuestion surveyQuestion = new SurveyQuestion()
            .surveyQuestionId(UPDATED_SURVEY_QUESTION_ID)
            .order(UPDATED_ORDER);
        return surveyQuestion;
    }

    @BeforeEach
    public void initTest() {
        surveyQuestionRepository.deleteAll();
        surveyQuestion = createEntity();
    }

    @Test
    public void createSurveyQuestion() throws Exception {
        int databaseSizeBeforeCreate = surveyQuestionRepository.findAll().size();

        // Create the SurveyQuestion
        restSurveyQuestionMockMvc.perform(post("/api/survey-questions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(surveyQuestion)))
            .andExpect(status().isCreated());

        // Validate the SurveyQuestion in the database
        List<SurveyQuestion> surveyQuestionList = surveyQuestionRepository.findAll();
        assertThat(surveyQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        SurveyQuestion testSurveyQuestion = surveyQuestionList.get(surveyQuestionList.size() - 1);
        assertThat(testSurveyQuestion.getSurveyQuestionId()).isEqualTo(DEFAULT_SURVEY_QUESTION_ID);
        assertThat(testSurveyQuestion.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    public void createSurveyQuestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = surveyQuestionRepository.findAll().size();

        // Create the SurveyQuestion with an existing ID
        surveyQuestion.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurveyQuestionMockMvc.perform(post("/api/survey-questions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(surveyQuestion)))
            .andExpect(status().isBadRequest());

        // Validate the SurveyQuestion in the database
        List<SurveyQuestion> surveyQuestionList = surveyQuestionRepository.findAll();
        assertThat(surveyQuestionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkSurveyQuestionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyQuestionRepository.findAll().size();
        // set the field null
        surveyQuestion.setSurveyQuestionId(null);

        // Create the SurveyQuestion, which fails.

        restSurveyQuestionMockMvc.perform(post("/api/survey-questions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(surveyQuestion)))
            .andExpect(status().isBadRequest());

        List<SurveyQuestion> surveyQuestionList = surveyQuestionRepository.findAll();
        assertThat(surveyQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyQuestionRepository.findAll().size();
        // set the field null
        surveyQuestion.setOrder(null);

        // Create the SurveyQuestion, which fails.

        restSurveyQuestionMockMvc.perform(post("/api/survey-questions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(surveyQuestion)))
            .andExpect(status().isBadRequest());

        List<SurveyQuestion> surveyQuestionList = surveyQuestionRepository.findAll();
        assertThat(surveyQuestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSurveyQuestions() throws Exception {
        // Initialize the database
        surveyQuestionRepository.save(surveyQuestion);

        // Get all the surveyQuestionList
        restSurveyQuestionMockMvc.perform(get("/api/survey-questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surveyQuestion.getId())))
            .andExpect(jsonPath("$.[*].surveyQuestionId").value(hasItem(DEFAULT_SURVEY_QUESTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }
    
    @Test
    public void getSurveyQuestion() throws Exception {
        // Initialize the database
        surveyQuestionRepository.save(surveyQuestion);

        // Get the surveyQuestion
        restSurveyQuestionMockMvc.perform(get("/api/survey-questions/{id}", surveyQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(surveyQuestion.getId()))
            .andExpect(jsonPath("$.surveyQuestionId").value(DEFAULT_SURVEY_QUESTION_ID.intValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    public void getNonExistingSurveyQuestion() throws Exception {
        // Get the surveyQuestion
        restSurveyQuestionMockMvc.perform(get("/api/survey-questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSurveyQuestion() throws Exception {
        // Initialize the database
        surveyQuestionRepository.save(surveyQuestion);

        int databaseSizeBeforeUpdate = surveyQuestionRepository.findAll().size();

        // Update the surveyQuestion
        SurveyQuestion updatedSurveyQuestion = surveyQuestionRepository.findById(surveyQuestion.getId()).get();
        updatedSurveyQuestion
            .surveyQuestionId(UPDATED_SURVEY_QUESTION_ID)
            .order(UPDATED_ORDER);

        restSurveyQuestionMockMvc.perform(put("/api/survey-questions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSurveyQuestion)))
            .andExpect(status().isOk());

        // Validate the SurveyQuestion in the database
        List<SurveyQuestion> surveyQuestionList = surveyQuestionRepository.findAll();
        assertThat(surveyQuestionList).hasSize(databaseSizeBeforeUpdate);
        SurveyQuestion testSurveyQuestion = surveyQuestionList.get(surveyQuestionList.size() - 1);
        assertThat(testSurveyQuestion.getSurveyQuestionId()).isEqualTo(UPDATED_SURVEY_QUESTION_ID);
        assertThat(testSurveyQuestion.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    public void updateNonExistingSurveyQuestion() throws Exception {
        int databaseSizeBeforeUpdate = surveyQuestionRepository.findAll().size();

        // Create the SurveyQuestion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyQuestionMockMvc.perform(put("/api/survey-questions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(surveyQuestion)))
            .andExpect(status().isBadRequest());

        // Validate the SurveyQuestion in the database
        List<SurveyQuestion> surveyQuestionList = surveyQuestionRepository.findAll();
        assertThat(surveyQuestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSurveyQuestion() throws Exception {
        // Initialize the database
        surveyQuestionRepository.save(surveyQuestion);

        int databaseSizeBeforeDelete = surveyQuestionRepository.findAll().size();

        // Delete the surveyQuestion
        restSurveyQuestionMockMvc.perform(delete("/api/survey-questions/{id}", surveyQuestion.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SurveyQuestion> surveyQuestionList = surveyQuestionRepository.findAll();
        assertThat(surveyQuestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
