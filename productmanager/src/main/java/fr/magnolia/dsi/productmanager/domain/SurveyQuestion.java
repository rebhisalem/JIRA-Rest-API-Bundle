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
 * A SurveyQuestion.
 */
@Document(collection = "survey_question")
public class SurveyQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("survey_question_id")
    private Long surveyQuestionId;

    @NotNull
    @Field("order")
    private Integer order;

    @DBRef
    @Field("question")
    @JsonIgnoreProperties("surveyQuestions")
    private Question question;

    @DBRef
    @Field("survey")
    @JsonIgnoreProperties("surveyQuestions")
    private Survey survey;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSurveyQuestionId() {
        return surveyQuestionId;
    }

    public SurveyQuestion surveyQuestionId(Long surveyQuestionId) {
        this.surveyQuestionId = surveyQuestionId;
        return this;
    }

    public void setSurveyQuestionId(Long surveyQuestionId) {
        this.surveyQuestionId = surveyQuestionId;
    }

    public Integer getOrder() {
        return order;
    }

    public SurveyQuestion order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Question getQuestion() {
        return question;
    }

    public SurveyQuestion question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Survey getSurvey() {
        return survey;
    }

    public SurveyQuestion survey(Survey survey) {
        this.survey = survey;
        return this;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SurveyQuestion)) {
            return false;
        }
        return id != null && id.equals(((SurveyQuestion) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SurveyQuestion{" +
            "id=" + getId() +
            ", surveyQuestionId=" + getSurveyQuestionId() +
            ", order=" + getOrder() +
            "}";
    }
}
