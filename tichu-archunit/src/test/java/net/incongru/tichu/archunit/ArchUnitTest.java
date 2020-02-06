package net.incongru.tichu.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.constructors;

@AnalyzeClasses(packages = "net.incongru.tichu")
public class ArchUnitTest {
    // TODO // Other interesting rules in com.tngtech.archunit.library.GeneralCodingRules

    @ArchTest
    public static final ArchRule classes_in_impl_packages_should_be_package_private =
            classes()
                    .that().resideInAPackage("..impl")
                    .and().haveSimpleNameNotEndingWith("FactoryImpl")
                    .should().bePackagePrivate();

    @ArchTest
    public static final ArchRule ctors_in_impl_packages_should_be_package_private =
            constructors()
                    .that().areDeclaredInClassesThat().resideInAPackage("..impl")
                    .and().areDeclaredInClassesThat().haveSimpleNameNotEndingWith("FactoryImpl")
                    .should().bePackagePrivate();
}
