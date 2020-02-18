package fr.magnolia.dsi.productmanager.web.rest;

import fr.magnolia.dsi.productmanager.ProductmanagerApp;
import fr.magnolia.dsi.productmanager.domain.Product;
import fr.magnolia.dsi.productmanager.repository.ProductRepository;
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


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static fr.magnolia.dsi.productmanager.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.magnolia.dsi.productmanager.domain.enumeration.EProductCategory;
import fr.magnolia.dsi.productmanager.domain.enumeration.EFundingType;
/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@SpringBootTest(classes = ProductmanagerApp.class)
public class ProductResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REISSUE = false;
    private static final Boolean UPDATED_REISSUE = true;

    private static final LocalDate DEFAULT_DATE_BEGIN_E_SIGNATURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_BEGIN_E_SIGNATURE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_END_E_SIGNATURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_END_E_SIGNATURE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_BEGIN_PAPER = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_BEGIN_PAPER = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_END_PAPER = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_END_PAPER = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_BEGIN_TRANSFERT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_BEGIN_TRANSFERT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_END_TRANSFERT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_END_TRANSFERT = LocalDate.now(ZoneId.systemDefault());

    private static final EProductCategory DEFAULT_CATEGORY = EProductCategory.IMMO;
    private static final EProductCategory UPDATED_CATEGORY = EProductCategory.DEATH;

    private static final EFundingType DEFAULT_FUNDIG_TYPE = EFundingType.IN_FINE;
    private static final EFundingType UPDATED_FUNDIG_TYPE = EFundingType.CONSTANT_AMORTIZATION;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restProductMockMvc;

    private Product product;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductResource productResource = new ProductResource(productRepository);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
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
    public static Product createEntity() {
        Product product = new Product()
            .productId(DEFAULT_PRODUCT_ID)
            .code(DEFAULT_CODE)
            .label(DEFAULT_LABEL)
            .reissue(DEFAULT_REISSUE)
            .dateBeginESignature(DEFAULT_DATE_BEGIN_E_SIGNATURE)
            .dateEndESignature(DEFAULT_DATE_END_E_SIGNATURE)
            .dateBeginPaper(DEFAULT_DATE_BEGIN_PAPER)
            .dateEndPaper(DEFAULT_DATE_END_PAPER)
            .dateBeginTransfert(DEFAULT_DATE_BEGIN_TRANSFERT)
            .dateEndTransfert(DEFAULT_DATE_END_TRANSFERT)
            .category(DEFAULT_CATEGORY)
            .fundigType(DEFAULT_FUNDIG_TYPE);
        return product;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity() {
        Product product = new Product()
            .productId(UPDATED_PRODUCT_ID)
            .code(UPDATED_CODE)
            .label(UPDATED_LABEL)
            .reissue(UPDATED_REISSUE)
            .dateBeginESignature(UPDATED_DATE_BEGIN_E_SIGNATURE)
            .dateEndESignature(UPDATED_DATE_END_E_SIGNATURE)
            .dateBeginPaper(UPDATED_DATE_BEGIN_PAPER)
            .dateEndPaper(UPDATED_DATE_END_PAPER)
            .dateBeginTransfert(UPDATED_DATE_BEGIN_TRANSFERT)
            .dateEndTransfert(UPDATED_DATE_END_TRANSFERT)
            .category(UPDATED_CATEGORY)
            .fundigType(UPDATED_FUNDIG_TYPE);
        return product;
    }

    @BeforeEach
    public void initTest() {
        productRepository.deleteAll();
        product = createEntity();
    }

    @Test
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProduct.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProduct.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testProduct.isReissue()).isEqualTo(DEFAULT_REISSUE);
        assertThat(testProduct.getDateBeginESignature()).isEqualTo(DEFAULT_DATE_BEGIN_E_SIGNATURE);
        assertThat(testProduct.getDateEndESignature()).isEqualTo(DEFAULT_DATE_END_E_SIGNATURE);
        assertThat(testProduct.getDateBeginPaper()).isEqualTo(DEFAULT_DATE_BEGIN_PAPER);
        assertThat(testProduct.getDateEndPaper()).isEqualTo(DEFAULT_DATE_END_PAPER);
        assertThat(testProduct.getDateBeginTransfert()).isEqualTo(DEFAULT_DATE_BEGIN_TRANSFERT);
        assertThat(testProduct.getDateEndTransfert()).isEqualTo(DEFAULT_DATE_END_TRANSFERT);
        assertThat(testProduct.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testProduct.getFundigType()).isEqualTo(DEFAULT_FUNDIG_TYPE);
    }

    @Test
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkProductIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProductId(null);

        // Create the Product, which fails.

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setCode(null);

        // Create the Product, which fails.

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setLabel(null);

        // Create the Product, which fails.

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setCategory(null);

        // Create the Product, which fails.

        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.save(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].reissue").value(hasItem(DEFAULT_REISSUE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateBeginESignature").value(hasItem(DEFAULT_DATE_BEGIN_E_SIGNATURE.toString())))
            .andExpect(jsonPath("$.[*].dateEndESignature").value(hasItem(DEFAULT_DATE_END_E_SIGNATURE.toString())))
            .andExpect(jsonPath("$.[*].dateBeginPaper").value(hasItem(DEFAULT_DATE_BEGIN_PAPER.toString())))
            .andExpect(jsonPath("$.[*].dateEndPaper").value(hasItem(DEFAULT_DATE_END_PAPER.toString())))
            .andExpect(jsonPath("$.[*].dateBeginTransfert").value(hasItem(DEFAULT_DATE_BEGIN_TRANSFERT.toString())))
            .andExpect(jsonPath("$.[*].dateEndTransfert").value(hasItem(DEFAULT_DATE_END_TRANSFERT.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].fundigType").value(hasItem(DEFAULT_FUNDIG_TYPE.toString())));
    }
    
    @Test
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.save(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.reissue").value(DEFAULT_REISSUE.booleanValue()))
            .andExpect(jsonPath("$.dateBeginESignature").value(DEFAULT_DATE_BEGIN_E_SIGNATURE.toString()))
            .andExpect(jsonPath("$.dateEndESignature").value(DEFAULT_DATE_END_E_SIGNATURE.toString()))
            .andExpect(jsonPath("$.dateBeginPaper").value(DEFAULT_DATE_BEGIN_PAPER.toString()))
            .andExpect(jsonPath("$.dateEndPaper").value(DEFAULT_DATE_END_PAPER.toString()))
            .andExpect(jsonPath("$.dateBeginTransfert").value(DEFAULT_DATE_BEGIN_TRANSFERT.toString()))
            .andExpect(jsonPath("$.dateEndTransfert").value(DEFAULT_DATE_END_TRANSFERT.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.fundigType").value(DEFAULT_FUNDIG_TYPE.toString()));
    }

    @Test
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.save(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        updatedProduct
            .productId(UPDATED_PRODUCT_ID)
            .code(UPDATED_CODE)
            .label(UPDATED_LABEL)
            .reissue(UPDATED_REISSUE)
            .dateBeginESignature(UPDATED_DATE_BEGIN_E_SIGNATURE)
            .dateEndESignature(UPDATED_DATE_END_E_SIGNATURE)
            .dateBeginPaper(UPDATED_DATE_BEGIN_PAPER)
            .dateEndPaper(UPDATED_DATE_END_PAPER)
            .dateBeginTransfert(UPDATED_DATE_BEGIN_TRANSFERT)
            .dateEndTransfert(UPDATED_DATE_END_TRANSFERT)
            .category(UPDATED_CATEGORY)
            .fundigType(UPDATED_FUNDIG_TYPE);

        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProduct)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProduct.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testProduct.isReissue()).isEqualTo(UPDATED_REISSUE);
        assertThat(testProduct.getDateBeginESignature()).isEqualTo(UPDATED_DATE_BEGIN_E_SIGNATURE);
        assertThat(testProduct.getDateEndESignature()).isEqualTo(UPDATED_DATE_END_E_SIGNATURE);
        assertThat(testProduct.getDateBeginPaper()).isEqualTo(UPDATED_DATE_BEGIN_PAPER);
        assertThat(testProduct.getDateEndPaper()).isEqualTo(UPDATED_DATE_END_PAPER);
        assertThat(testProduct.getDateBeginTransfert()).isEqualTo(UPDATED_DATE_BEGIN_TRANSFERT);
        assertThat(testProduct.getDateEndTransfert()).isEqualTo(UPDATED_DATE_END_TRANSFERT);
        assertThat(testProduct.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testProduct.getFundigType()).isEqualTo(UPDATED_FUNDIG_TYPE);
    }

    @Test
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.save(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
