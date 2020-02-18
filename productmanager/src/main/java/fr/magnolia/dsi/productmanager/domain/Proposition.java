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
 * A Proposition.
 */
@Document(collection = "proposition")
public class Proposition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("proposition_id")
    private Long propositionId;

    @NotNull
    @Field("label")
    private String label;

    @DBRef
    @Field("propositionId")
    @JsonIgnoreProperties("questionIds")
    private Question propositionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPropositionId() {
        return propositionId;
    }

    public Proposition propositionId(Long propositionId) {
        this.propositionId = propositionId;
        return this;
    }

    public void setPropositionId(Long propositionId) {
        this.propositionId = propositionId;
    }

    public String getLabel() {
        return label;
    }

    public Proposition label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Question getPropositionId() {
        return propositionId;
    }

    public Proposition propositionId(Question question) {
        this.propositionId = question;
        return this;
    }

    public void setPropositionId(Question question) {
        this.propositionId = question;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proposition)) {
            return false;
        }
        return id != null && id.equals(((Proposition) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Proposition{" +
            "id=" + getId() +
            ", propositionId=" + getPropositionId() +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
