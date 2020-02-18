package fr.magnolia.dsi.productmanager.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import fr.magnolia.dsi.productmanager.domain.enumeration.EProductCategory;

import fr.magnolia.dsi.productmanager.domain.enumeration.EFundingType;

/**
 * A Product.
 */
@Document(collection = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("product_id")
    private Long productId;

    @NotNull
    @Field("code")
    private String code;

    @NotNull
    @Field("label")
    private String label;

    @Field("reissue")
    private Boolean reissue;

    @Field("date_begin_e_signature")
    private LocalDate dateBeginESignature;

    @Field("date_end_e_signature")
    private LocalDate dateEndESignature;

    @Field("date_begin_paper")
    private LocalDate dateBeginPaper;

    @Field("date_end_paper")
    private LocalDate dateEndPaper;

    @Field("date_begin_transfert")
    private LocalDate dateBeginTransfert;

    @Field("date_end_transfert")
    private LocalDate dateEndTransfert;

    @NotNull
    @Field("category")
    private EProductCategory category;

    @Field("fundig_type")
    private EFundingType fundigType;

    @DBRef
    @Field("productStep")
    private Set<ProductStep> productSteps = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public Product productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCode() {
        return code;
    }

    public Product code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public Product label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean isReissue() {
        return reissue;
    }

    public Product reissue(Boolean reissue) {
        this.reissue = reissue;
        return this;
    }

    public void setReissue(Boolean reissue) {
        this.reissue = reissue;
    }

    public LocalDate getDateBeginESignature() {
        return dateBeginESignature;
    }

    public Product dateBeginESignature(LocalDate dateBeginESignature) {
        this.dateBeginESignature = dateBeginESignature;
        return this;
    }

    public void setDateBeginESignature(LocalDate dateBeginESignature) {
        this.dateBeginESignature = dateBeginESignature;
    }

    public LocalDate getDateEndESignature() {
        return dateEndESignature;
    }

    public Product dateEndESignature(LocalDate dateEndESignature) {
        this.dateEndESignature = dateEndESignature;
        return this;
    }

    public void setDateEndESignature(LocalDate dateEndESignature) {
        this.dateEndESignature = dateEndESignature;
    }

    public LocalDate getDateBeginPaper() {
        return dateBeginPaper;
    }

    public Product dateBeginPaper(LocalDate dateBeginPaper) {
        this.dateBeginPaper = dateBeginPaper;
        return this;
    }

    public void setDateBeginPaper(LocalDate dateBeginPaper) {
        this.dateBeginPaper = dateBeginPaper;
    }

    public LocalDate getDateEndPaper() {
        return dateEndPaper;
    }

    public Product dateEndPaper(LocalDate dateEndPaper) {
        this.dateEndPaper = dateEndPaper;
        return this;
    }

    public void setDateEndPaper(LocalDate dateEndPaper) {
        this.dateEndPaper = dateEndPaper;
    }

    public LocalDate getDateBeginTransfert() {
        return dateBeginTransfert;
    }

    public Product dateBeginTransfert(LocalDate dateBeginTransfert) {
        this.dateBeginTransfert = dateBeginTransfert;
        return this;
    }

    public void setDateBeginTransfert(LocalDate dateBeginTransfert) {
        this.dateBeginTransfert = dateBeginTransfert;
    }

    public LocalDate getDateEndTransfert() {
        return dateEndTransfert;
    }

    public Product dateEndTransfert(LocalDate dateEndTransfert) {
        this.dateEndTransfert = dateEndTransfert;
        return this;
    }

    public void setDateEndTransfert(LocalDate dateEndTransfert) {
        this.dateEndTransfert = dateEndTransfert;
    }

    public EProductCategory getCategory() {
        return category;
    }

    public Product category(EProductCategory category) {
        this.category = category;
        return this;
    }

    public void setCategory(EProductCategory category) {
        this.category = category;
    }

    public EFundingType getFundigType() {
        return fundigType;
    }

    public Product fundigType(EFundingType fundigType) {
        this.fundigType = fundigType;
        return this;
    }

    public void setFundigType(EFundingType fundigType) {
        this.fundigType = fundigType;
    }

    public Set<ProductStep> getProductSteps() {
        return productSteps;
    }

    public Product productSteps(Set<ProductStep> productSteps) {
        this.productSteps = productSteps;
        return this;
    }

    public Product addProductStep(ProductStep productStep) {
        this.productSteps.add(productStep);
        productStep.setProduct(this);
        return this;
    }

    public Product removeProductStep(ProductStep productStep) {
        this.productSteps.remove(productStep);
        productStep.setProduct(null);
        return this;
    }

    public void setProductSteps(Set<ProductStep> productSteps) {
        this.productSteps = productSteps;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", code='" + getCode() + "'" +
            ", label='" + getLabel() + "'" +
            ", reissue='" + isReissue() + "'" +
            ", dateBeginESignature='" + getDateBeginESignature() + "'" +
            ", dateEndESignature='" + getDateEndESignature() + "'" +
            ", dateBeginPaper='" + getDateBeginPaper() + "'" +
            ", dateEndPaper='" + getDateEndPaper() + "'" +
            ", dateBeginTransfert='" + getDateBeginTransfert() + "'" +
            ", dateEndTransfert='" + getDateEndTransfert() + "'" +
            ", category='" + getCategory() + "'" +
            ", fundigType='" + getFundigType() + "'" +
            "}";
    }
}
