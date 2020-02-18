package fr.magnolia.dsi.productmanager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.magnolia.dsi.productmanager.web.rest.TestUtil;

public class StepDecisionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StepDecision.class);
        StepDecision stepDecision1 = new StepDecision();
        stepDecision1.setId("id1");
        StepDecision stepDecision2 = new StepDecision();
        stepDecision2.setId(stepDecision1.getId());
        assertThat(stepDecision1).isEqualTo(stepDecision2);
        stepDecision2.setId("id2");
        assertThat(stepDecision1).isNotEqualTo(stepDecision2);
        stepDecision1.setId(null);
        assertThat(stepDecision1).isNotEqualTo(stepDecision2);
    }
}
