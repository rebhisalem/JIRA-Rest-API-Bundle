package fr.magnolia.dsi.productmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ProductStep.
 */
@Document(collection = "product_step")
public class ProductStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("product_step_id")
    private Long productStepId;

    @Field("first_step")
    private Boolean firstStep;

    @DBRef
    @Field("step")
    @JsonIgnoreProperties("productSteps")
    private Step step;

    @DBRef
    @Field("product")
    @JsonIgnoreProperties("productSteps")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProductStepId() {
        return productStepId;
    }

    public ProductStep productStepId(Long productStepId) {
        this.productStepId = productStepId;
        return this;
    }

    public void setProductStepId(Long productStepId) {
        this.productStepId = productStepId;
    }

    public Boolean isFirstStep() {
        return firstStep;
    }

    public ProductStep firstStep(Boolean firstStep) {
        this.firstStep = firstStep;
        return this;
    }

    public void setFirstStep(Boolean firstStep) {
        this.firstStep = firstStep;
    }

    public Step getStep() {
        return step;
    }

    public ProductStep step(Step step) {
        this.step = step;
        return this;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Product getProduct() {
        return product;
    }

    public ProductStep product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductStep)) {
            return false;
        }
        return id != null && id.equals(((ProductStep) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProductStep{" +
            "id=" + getId() +
            ", productStepId=" + getProductStepId() +
            ", firstStep='" + isFirstStep() + "'" +
            "}";
    }
}
