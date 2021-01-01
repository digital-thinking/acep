package de.ixeption.acep;

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
            .importPackages("de.ixeption.acep");

        noClasses()
            .that()
            .resideInAnyPackage("de.ixeption.acep.service..")
            .or()
            .resideInAnyPackage("de.ixeption.acep.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..de.ixeption.acep.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
