package fr.magnolia.dsi.productmanager.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import fr.magnolia.dsi.productmanager.domain.enumeration.EQuestionType;

/**
 * A Question.
 */
@Document(collection = "question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("question_id")
    private Long questionId;

    @NotNull
    @Field("code")
    private String code;

    @NotNull
    @Field("text")
    private String text;

    @Field("dependent")
    private Boolean dependent;

    @NotNull
    @Field("type")
    private EQuestionType type;

    @DBRef
    @Field("questionId")
    private Set<Proposition> questionIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public Question questionId(Long questionId) {
        this.questionId = questionId;
        return this;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getCode() {
        return code;
    }

    public Question code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public Question text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isDependent() {
        return dependent;
    }

    public Question dependent(Boolean dependent) {
        this.dependent = dependent;
        return this;
    }

    public void setDependent(Boolean dependent) {
        this.dependent = dependent;
    }

    public EQuestionType getType() {
        return type;
    }

    public Question type(EQuestionType type) {
        this.type = type;
        return this;
    }

    public void setType(EQuestionType type) {
        this.type = type;
    }

    public Set<Proposition> getQuestionIds() {
        return questionIds;
    }

    public Question questionIds(Set<Proposition> propositions) {
        this.questionIds = propositions;
        return this;
    }

    public Question addQuestionId(Proposition proposition) {
        this.questionIds.add(proposition);
        proposition.setPropositionId(this);
        return this;
    }

    public Question removeQuestionId(Proposition proposition) {
        this.questionIds.remove(proposition);
        proposition.setPropositionId(null);
        return this;
    }

    public void setQuestionIds(Set<Proposition> propositions) {
        this.questionIds = propositions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", questionId=" + getQuestionId() +
            ", code='" + getCode() + "'" +
            ", text='" + getText() + "'" +
            ", dependent='" + isDependent() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
