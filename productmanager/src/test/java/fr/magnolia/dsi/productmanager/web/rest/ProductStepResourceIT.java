package fr.magnolia.dsi.productmanager.web.rest;

import fr.magnolia.dsi.productmanager.ProductmanagerApp;
import fr.magnolia.dsi.productmanager.domain.ProductStep;
import fr.magnolia.dsi.productmanager.repository.ProductStepRepository;
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
 * Integration tests for the {@link ProductStepResource} REST controller.
 */
@SpringBootTest(classes = ProductmanagerApp.class)
public class ProductStepResourceIT {

    private static final Long DEFAULT_PRODUCT_STEP_ID = 1L;
    private static final Long UPDATED_PRODUCT_STEP_ID = 2L;

    private static final Boolean DEFAULT_FIRST_STEP = false;
    private static final Boolean UPDATED_FIRST_STEP = true;

    @Autowired
    private ProductStepRepository productStepRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restProductStepMockMvc;

    private ProductStep productStep;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductStepResource productStepResource = new ProductStepResource(productStepRepository);
        this.restProductStepMockMvc = MockMvcBuilders.standaloneSetup(productStepResource)
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
    public static ProductStep createEntity() {
        ProductStep productStep = new ProductStep()
            .productStepId(DEFAULT_PRODUCT_STEP_ID)
            .firstStep(DEFAULT_FIRST_STEP);
        return productStep;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductStep createUpdatedEntity() {
        ProductStep productStep = new ProductStep()
            .productStepId(UPDATED_PRODUCT_STEP_ID)
            .firstStep(UPDATED_FIRST_STEP);
        return productStep;
    }

    @BeforeEach
    public void initTest() {
        productStepRepository.deleteAll();
        productStep = createEntity();
    }

    @Test
    public void createProductStep() throws Exception {
        int databaseSizeBeforeCreate = productStepRepository.findAll().size();

        // Create the ProductStep
        restProductStepMockMvc.perform(post("/api/product-steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productStep)))
            .andExpect(status().isCreated());

        // Validate the ProductStep in the database
        List<ProductStep> productStepList = productStepRepository.findAll();
        assertThat(productStepList).hasSize(databaseSizeBeforeCreate + 1);
        ProductStep testProductStep = productStepList.get(productStepList.size() - 1);
        assertThat(testProductStep.getProductStepId()).isEqualTo(DEFAULT_PRODUCT_STEP_ID);
        assertThat(testProductStep.isFirstStep()).isEqualTo(DEFAULT_FIRST_STEP);
    }

    @Test
    public void createProductStepWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productStepRepository.findAll().size();

        // Create the ProductStep with an existing ID
        productStep.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductStepMockMvc.perform(post("/api/product-steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productStep)))
            .andExpect(status().isBadRequest());

        // Validate the ProductStep in the database
        List<ProductStep> productStepList = productStepRepository.findAll();
        assertThat(productStepList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkProductStepIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productStepRepository.findAll().size();
        // set the field null
        productStep.setProductStepId(null);

        // Create the ProductStep, which fails.

        restProductStepMockMvc.perform(post("/api/product-steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productStep)))
            .andExpect(status().isBadRequest());

        List<ProductStep> productStepList = productStepRepository.findAll();
        assertThat(productStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllProductSteps() throws Exception {
        // Initialize the database
        productStepRepository.save(productStep);

        // Get all the productStepList
        restProductStepMockMvc.perform(get("/api/product-steps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productStep.getId())))
            .andExpect(jsonPath("$.[*].productStepId").value(hasItem(DEFAULT_PRODUCT_STEP_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstStep").value(hasItem(DEFAULT_FIRST_STEP.booleanValue())));
    }
    
    @Test
    public void getProductStep() throws Exception {
        // Initialize the database
        productStepRepository.save(productStep);

        // Get the productStep
        restProductStepMockMvc.perform(get("/api/product-steps/{id}", productStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productStep.getId()))
            .andExpect(jsonPath("$.productStepId").value(DEFAULT_PRODUCT_STEP_ID.intValue()))
            .andExpect(jsonPath("$.firstStep").value(DEFAULT_FIRST_STEP.booleanValue()));
    }

    @Test
    public void getNonExistingProductStep() throws Exception {
        // Get the productStep
        restProductStepMockMvc.perform(get("/api/product-steps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateProductStep() throws Exception {
        // Initialize the database
        productStepRepository.save(productStep);

        int databaseSizeBeforeUpdate = productStepRepository.findAll().size();

        // Update the productStep
        ProductStep updatedProductStep = productStepRepository.findById(productStep.getId()).get();
        updatedProductStep
            .productStepId(UPDATED_PRODUCT_STEP_ID)
            .firstStep(UPDATED_FIRST_STEP);

        restProductStepMockMvc.perform(put("/api/product-steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductStep)))
            .andExpect(status().isOk());

        // Validate the ProductStep in the database
        List<ProductStep> productStepList = productStepRepository.findAll();
        assertThat(productStepList).hasSize(databaseSizeBeforeUpdate);
        ProductStep testProductStep = productStepList.get(productStepList.size() - 1);
        assertThat(testProductStep.getProductStepId()).isEqualTo(UPDATED_PRODUCT_STEP_ID);
        assertThat(testProductStep.isFirstStep()).isEqualTo(UPDATED_FIRST_STEP);
    }

    @Test
    public void updateNonExistingProductStep() throws Exception {
        int databaseSizeBeforeUpdate = productStepRepository.findAll().size();

        // Create the ProductStep

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductStepMockMvc.perform(put("/api/product-steps")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productStep)))
            .andExpect(status().isBadRequest());

        // Validate the ProductStep in the database
        List<ProductStep> productStepList = productStepRepository.findAll();
        assertThat(productStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteProductStep() throws Exception {
        // Initialize the database
        productStepRepository.save(productStep);

        int databaseSizeBeforeDelete = productStepRepository.findAll().size();

        // Delete the productStep
        restProductStepMockMvc.perform(delete("/api/product-steps/{id}", productStep.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductStep> productStepList = productStepRepository.findAll();
        assertThat(productStepList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
