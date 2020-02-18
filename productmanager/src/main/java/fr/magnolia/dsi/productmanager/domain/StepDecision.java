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
 * A StepDecision.
 */
@Document(collection = "step_decision")
public class StepDecision implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("step_decision_id")
    private Long stepDecisionId;

    @NotNull
    @Field("order")
    private Integer order;

    @Field("final_step")
    private Boolean finalStep;

    @DBRef
    @Field("nextStep")
    @JsonIgnoreProperties("stepDecisions")
    private ProductStep nextStep;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getStepDecisionId() {
        return stepDecisionId;
    }

    public StepDecision stepDecisionId(Long stepDecisionId) {
        this.stepDecisionId = stepDecisionId;
        return this;
    }

    public void setStepDecisionId(Long stepDecisionId) {
        this.stepDecisionId = stepDecisionId;
    }

    public Integer getOrder() {
        return order;
    }

    public StepDecision order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean isFinalStep() {
        return finalStep;
    }

    public StepDecision finalStep(Boolean finalStep) {
        this.finalStep = finalStep;
        return this;
    }

    public void setFinalStep(Boolean finalStep) {
        this.finalStep = finalStep;
    }

    public ProductStep getNextStep() {
        return nextStep;
    }

    public StepDecision nextStep(ProductStep productStep) {
        this.nextStep = productStep;
        return this;
    }

    public void setNextStep(ProductStep productStep) {
        this.nextStep = productStep;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StepDecision)) {
            return false;
        }
        return id != null && id.equals(((StepDecision) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StepDecision{" +
            "id=" + getId() +
            ", stepDecisionId=" + getStepDecisionId() +
            ", order=" + getOrder() +
            ", finalStep='" + isFinalStep() + "'" +
            "}";
    }
}
