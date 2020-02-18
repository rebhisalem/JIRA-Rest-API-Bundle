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

/**
 * A Survey.
 */
@Document(collection = "survey")
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("survey_id")
    private Long surveyId;

    @DBRef
    @Field("surveyId")
    private Set<Step> surveyIds = new HashSet<>();

    @DBRef
    @Field("surveyQuestion")
    private Set<SurveyQuestion> surveyQuestions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public Survey surveyId(Long surveyId) {
        this.surveyId = surveyId;
        return this;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Set<Step> getSurveyIds() {
        return surveyIds;
    }

    public Survey surveyIds(Set<Step> steps) {
        this.surveyIds = steps;
        return this;
    }

    public Survey addSurveyId(Step step) {
        this.surveyIds.add(step);
        step.setStepId(this);
        return this;
    }

    public Survey removeSurveyId(Step step) {
        this.surveyIds.remove(step);
        step.setStepId(null);
        return this;
    }

    public void setSurveyIds(Set<Step> steps) {
        this.surveyIds = steps;
    }

    public Set<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }

    public Survey surveyQuestions(Set<SurveyQuestion> surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
        return this;
    }

    public Survey addSurveyQuestion(SurveyQuestion surveyQuestion) {
        this.surveyQuestions.add(surveyQuestion);
        surveyQuestion.setSurvey(this);
        return this;
    }

    public Survey removeSurveyQuestion(SurveyQuestion surveyQuestion) {
        this.surveyQuestions.remove(surveyQuestion);
        surveyQuestion.setSurvey(null);
        return this;
    }

    public void setSurveyQuestions(Set<SurveyQuestion> surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Survey)) {
            return false;
        }
        return id != null && id.equals(((Survey) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Survey{" +
            "id=" + getId() +
            ", surveyId=" + getSurveyId() +
            "}";
    }
}
