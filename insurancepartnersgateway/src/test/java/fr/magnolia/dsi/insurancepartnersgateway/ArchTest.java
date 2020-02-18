package fr.magnolia.dsi.insurancepartnersgateway;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("fr.magnolia.dsi.insurancepartnersgateway");

        noClasses()
            .that()
                .resideInAnyPackage("fr.magnolia.dsi.insurancepartnersgateway.service..")
            .or()
                .resideInAnyPackage("fr.magnolia.dsi.insurancepartnersgateway.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..fr.magnolia.dsi.insurancepartnersgateway.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
