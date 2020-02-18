package fr.magnolia.dsi.productmanager;

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
            .importPackages("fr.magnolia.dsi.productmanager");

        noClasses()
            .that()
                .resideInAnyPackage("fr.magnolia.dsi.productmanager.service..")
            .or()
                .resideInAnyPackage("fr.magnolia.dsi.productmanager.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..fr.magnolia.dsi.productmanager.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
