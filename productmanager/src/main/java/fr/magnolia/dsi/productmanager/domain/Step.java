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
 * A Step.
 */
@Document(collection = "step")
public class Step implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("step_id")
    private Long stepId;

    @NotNull
    @Field("code")
    private String code;

    @NotNull
    @Field("label")
    private String label;

    @DBRef
    @Field("stepId")
    @JsonIgnoreProperties("surveyIds")
    private Survey stepId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getStepId() {
        return stepId;
    }

    public Step stepId(Long stepId) {
        this.stepId = stepId;
        return this;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public String getCode() {
        return code;
    }

    public Step code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public Step label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Survey getStepId() {
        return stepId;
    }

    public Step stepId(Survey survey) {
        this.stepId = survey;
        return this;
    }

    public void setStepId(Survey survey) {
        this.stepId = survey;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Step)) {
            return false;
        }
        return id != null && id.equals(((Step) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Step{" +
            "id=" + getId() +
            ", stepId=" + getStepId() +
            ", code='" + getCode() + "'" +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
