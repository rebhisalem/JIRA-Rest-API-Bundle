package fr.magnolia.dsi.productmanager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.magnolia.dsi.productmanager.web.rest.TestUtil;

public class SurveyTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Survey.class);
        Survey survey1 = new Survey();
        survey1.setId("id1");
        Survey survey2 = new Survey();
        survey2.setId(survey1.getId());
        assertThat(survey1).isEqualTo(survey2);
        survey2.setId("id2");
        assertThat(survey1).isNotEqualTo(survey2);
        survey1.setId(null);
        assertThat(survey1).isNotEqualTo(survey2);
    }
}
