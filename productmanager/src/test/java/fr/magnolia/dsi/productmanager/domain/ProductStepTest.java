package fr.magnolia.dsi.productmanager.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.magnolia.dsi.productmanager.web.rest.TestUtil;

public class ProductStepTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductStep.class);
        ProductStep productStep1 = new ProductStep();
        productStep1.setId("id1");
        ProductStep productStep2 = new ProductStep();
        productStep2.setId(productStep1.getId());
        assertThat(productStep1).isEqualTo(productStep2);
        productStep2.setId("id2");
        assertThat(productStep1).isNotEqualTo(productStep2);
        productStep1.setId(null);
        assertThat(productStep1).isNotEqualTo(productStep2);
    }
}
