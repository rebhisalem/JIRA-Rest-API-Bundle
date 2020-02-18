package fr.magnolia.dsi.productmanager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.magnolia.dsi.productmanager.web.rest.TestUtil;

public class SurveyQuestionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurveyQuestion.class);
        SurveyQuestion surveyQuestion1 = new SurveyQuestion();
        surveyQuestion1.setId("id1");
        SurveyQuestion surveyQuestion2 = new SurveyQuestion();
        surveyQuestion2.setId(surveyQuestion1.getId());
        assertThat(surveyQuestion1).isEqualTo(surveyQuestion2);
        surveyQuestion2.setId("id2");
        assertThat(surveyQuestion1).isNotEqualTo(surveyQuestion2);
        surveyQuestion1.setId(null);
        assertThat(surveyQuestion1).isNotEqualTo(surveyQuestion2);
    }
}
