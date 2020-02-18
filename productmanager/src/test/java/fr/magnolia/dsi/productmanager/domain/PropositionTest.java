package fr.magnolia.dsi.productmanager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.magnolia.dsi.productmanager.web.rest.TestUtil;

public class PropositionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proposition.class);
        Proposition proposition1 = new Proposition();
        proposition1.setId("id1");
        Proposition proposition2 = new Proposition();
        proposition2.setId(proposition1.getId());
        assertThat(proposition1).isEqualTo(proposition2);
        proposition2.setId("id2");
        assertThat(proposition1).isNotEqualTo(proposition2);
        proposition1.setId(null);
        assertThat(proposition1).isNotEqualTo(proposition2);
    }
}
