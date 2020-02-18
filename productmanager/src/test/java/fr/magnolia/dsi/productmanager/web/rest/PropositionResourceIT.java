package fr.magnolia.dsi.productmanager.web.rest;

import fr.magnolia.dsi.productmanager.ProductmanagerApp;
import fr.magnolia.dsi.productmanager.domain.Proposition;
import fr.magnolia.dsi.productmanager.repository.PropositionRepository;
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
 * Integration tests for the {@link PropositionResource} REST controller.
 */
@SpringBootTest(classes = ProductmanagerApp.class)
public class PropositionResourceIT {

    private static final Long DEFAULT_PROPOSITION_ID = 1L;
    private static final Long UPDATED_PROPOSITION_ID = 2L;

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private PropositionRepository propositionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restPropositionMockMvc;

    private Proposition proposition;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PropositionResource propositionResource = new PropositionResource(propositionRepository);
        this.restPropositionMockMvc = MockMvcBuilders.standaloneSetup(propositionResource)
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
    public static Proposition createEntity() {
        Proposition proposition = new Proposition()
            .propositionId(DEFAULT_PROPOSITION_ID)
            .label(DEFAULT_LABEL);
        return proposition;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposition createUpdatedEntity() {
        Proposition proposition = new Proposition()
            .propositionId(UPDATED_PROPOSITION_ID)
            .label(UPDATED_LABEL);
        return proposition;
    }

    @BeforeEach
    public void initTest() {
        propositionRepository.deleteAll();
        proposition = createEntity();
    }

    @Test
    public void createProposition() throws Exception {
        int databaseSizeBeforeCreate = propositionRepository.findAll().size();

        // Create the Proposition
        restPropositionMockMvc.perform(post("/api/propositions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposition)))
            .andExpect(status().isCreated());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeCreate + 1);
        Proposition testProposition = propositionList.get(propositionList.size() - 1);
        assertThat(testProposition.getPropositionId()).isEqualTo(DEFAULT_PROPOSITION_ID);
        assertThat(testProposition.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    public void createPropositionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = propositionRepository.findAll().size();

        // Create the Proposition with an existing ID
        proposition.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropositionMockMvc.perform(post("/api/propositions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposition)))
            .andExpect(status().isBadRequest());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkPropositionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = propositionRepository.findAll().size();
        // set the field null
        proposition.setPropositionId(null);

        // Create the Proposition, which fails.

        restPropositionMockMvc.perform(post("/api/propositions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposition)))
            .andExpect(status().isBadRequest());

        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = propositionRepository.findAll().size();
        // set the field null
        proposition.setLabel(null);

        // Create the Proposition, which fails.

        restPropositionMockMvc.perform(post("/api/propositions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposition)))
            .andExpect(status().isBadRequest());

        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllPropositions() throws Exception {
        // Initialize the database
        propositionRepository.save(proposition);

        // Get all the propositionList
        restPropositionMockMvc.perform(get("/api/propositions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposition.getId())))
            .andExpect(jsonPath("$.[*].propositionId").value(hasItem(DEFAULT_PROPOSITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }
    
    @Test
    public void getProposition() throws Exception {
        // Initialize the database
        propositionRepository.save(proposition);

        // Get the proposition
        restPropositionMockMvc.perform(get("/api/propositions/{id}", proposition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proposition.getId()))
            .andExpect(jsonPath("$.propositionId").value(DEFAULT_PROPOSITION_ID.intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }

    @Test
    public void getNonExistingProposition() throws Exception {
        // Get the proposition
        restPropositionMockMvc.perform(get("/api/propositions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateProposition() throws Exception {
        // Initialize the database
        propositionRepository.save(proposition);

        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();

        // Update the proposition
        Proposition updatedProposition = propositionRepository.findById(proposition.getId()).get();
        updatedProposition
            .propositionId(UPDATED_PROPOSITION_ID)
            .label(UPDATED_LABEL);

        restPropositionMockMvc.perform(put("/api/propositions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProposition)))
            .andExpect(status().isOk());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
        Proposition testProposition = propositionList.get(propositionList.size() - 1);
        assertThat(testProposition.getPropositionId()).isEqualTo(UPDATED_PROPOSITION_ID);
        assertThat(testProposition.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    public void updateNonExistingProposition() throws Exception {
        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();

        // Create the Proposition

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropositionMockMvc.perform(put("/api/propositions")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(proposition)))
            .andExpect(status().isBadRequest());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteProposition() throws Exception {
        // Initialize the database
        propositionRepository.save(proposition);

        int databaseSizeBeforeDelete = propositionRepository.findAll().size();

        // Delete the proposition
        restPropositionMockMvc.perform(delete("/api/propositions/{id}", proposition.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
