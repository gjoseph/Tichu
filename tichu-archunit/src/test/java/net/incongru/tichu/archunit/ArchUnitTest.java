package net.incongru.tichu.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.constructors;

@AnalyzeClasses(packages = "net.incongru.tichu")
public class ArchUnitTest {
    // TODO // Other interesting rules in com.tngtech.archunit.library.GeneralCodingRules

    @ArchTest
    static final ArchRule classes_and_ctors_in_impl_packages_should_be_package_private =
            constructors()
                    .that().areDeclaredInClassesThat().resideInAPackage("..impl")
                    .and().areDeclaredInClassesThat().haveSimpleNameNotEndingWith("FactoryImpl")
                    .should().bePackagePrivate()
                    .andShould().beDeclaredInClassesThat().arePackagePrivate();

}
