package com.github.codandotv.popcorngpdetekt.presentation

import io.github.detekt.test.utils.compileContentForTest
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import junit.framework.TestCase.assertFalse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@KotlinCoreEnvironmentTest
class KtFileExtensionsTest {

    private val packagePrefix = "com.github.codandotv"

    @Test
    fun `fullFileName should return full path combined with package and filename`() {
        val code = """
            package com.github.codandotv.popcorngpdetekt.presentation
            class ProfileViewModel
        """.trimIndent()

        val ktFile = compileContentForTest(code, filename = "ProfileViewModel.kt")
        val expected = "com.github.codandotv.popcorngpdetekt.presentation.ProfileViewModel.kt"

        assertEquals(expected, ktFile.fullFileName())
    }

    @Test
    fun `fullFileName should return only filename when package is empty`() {
        val code = """
            class NoPackageClass
        """.trimIndent()

        val ktFile = compileContentForTest(code, filename = "NoPackageClass.kt")

        assertEquals("NoPackageClass.kt", ktFile.fullFileName())
    }

    @Test
    fun `isInternalFile should return true when file is inside the package prefix path`() {
        val code = """
            package com.github.codandotv.popcorngpdetekt.presentation
            class HomeViewModel
        """.trimIndent()

        val ktFile = compileContentForTest(code, filename = "HomeViewModel.kt")

        assertTrue(ktFile.isInternalFile(packagePrefix))
    }

    @Test
    fun `isInternalFile should return false when file is outside the package prefix path`() {
        val code = """
            package com.example.external
            class ExternalClass
        """.trimIndent()

        val ktFile = compileContentForTest(code, filename = "ExternalClass.kt")

        assertFalse(ktFile.isInternalFile(packagePrefix))
    }

    @Test
    fun `imports should filter and return only imports matching the package prefix`() {
        val code = """
            package com.github.codandotv.popcorngpdetekt.presentation
            
            import com.github.codandotv.popcorngpdetekt.domain.GetMoviesUseCase
            import com.github.codandotv.popcorngpdetekt.data.MovieRepository
            import android.os.Bundle
            import java.util.UUID
            
            class MovieViewModel
        """.trimIndent()

        val ktFile = compileContentForTest(code, filename = "MovieViewModel.kt")
        val internalImports = ktFile.imports(packagePrefix).toSet()

        assertEquals(
            setOf(
                "com.github.codandotv.popcorngpdetekt.domain.GetMoviesUseCase",
                "com.github.codandotv.popcorngpdetekt.data.MovieRepository"
            ),
            internalImports,
        )
    }

    @Test
    fun `imports should return empty list when no imports match the package prefix`() {
        val code = """
            package com.github.codandotv.popcorngpdetekt.presentation
            
            import android.view.View
            import androidx.fragment.app.Fragment
            
            class BaseFragment
        """.trimIndent()

        val ktFile = compileContentForTest(code, filename = "BaseFragment.kt")

        assertEquals(0, ktFile.imports(packagePrefix).size)
    }

    @Test
    fun `imports should return empty list when file has zero imports`() {
        val code = """
            package com.github.codandotv.popcorngpdetekt.presentation
            class SimpleClass
        """.trimIndent()

        val ktFile = compileContentForTest(code, filename = "SimpleClass.kt")

        assertEquals(0, ktFile.imports(packagePrefix).size)
    }
}
