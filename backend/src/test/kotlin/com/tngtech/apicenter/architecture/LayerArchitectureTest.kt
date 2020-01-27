import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.ImportOption.*
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses


@AnalyzeClasses(packages = arrayOf("com.tngtech.apicenter"), importOptions = [DontIncludeTests::class] )
//@AnalyzeClasses(packagesOf = [LayerArchitectureTest::class])
class LayerArchitectureTest {

    @ArchTest
    val enforceClientBusinessLayering: ArchRule? = noClasses().that().resideInAnyPackage("....", "..business..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..dto..")

    @ArchTest
    val ensureUtilsAreIndependent: ArchRule = noClasses().that().resideInAPackage("..util..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..manager..", "..business..", "..client..")
}
