package com.tngtech.apicenter.architecture

import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

@AnalyzeClasses(packages = arrayOf("com.tngtech.apicenter"), importOptions = [ImportOption.DontIncludeTests::class])
class LayerArchitectureTest {

    @ArchTest
    val enforceDtoShouldNotDependOnRepositoryOrEntity: ArchRule? = noClasses().that().resideInAnyPackage("..repository..", "..entity..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..dto..")

    @ArchTest
    val enforceRestControllerShouldNotAccessDatabaseLayer: ArchRule? = noClasses().that().resideInAnyPackage("..controller..", "..dto..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..entity..", "..repository..")

    @ArchTest
    val enforceServiceDoesNotAccessUpwards: ArchRule? = noClasses().that().resideInAnyPackage("..service..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..controller..")

    @ArchTest
    val enforceRepositoryDoesNotAccessUpwards: ArchRule? = noClasses().that().resideInAnyPackage("..repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..service..", "..controller..")

    @ArchTest
    val enforceEntityDoesNotAccessUpwards: ArchRule? = noClasses().that().resideInAnyPackage("..entity..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..controller..")

    @ArchTest
    val enforceMapperShouldNotAccessUpOrDownwards: ArchRule? = noClasses().that().resideInAnyPackage("..mapper..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..controller..", "..repository..")

}
