package mobi.hsz.idea.nodesecurity.utils

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test

class UtilsTest : LightPlatformCodeInsightFixtureTestCase() {
    private fun psiFile(path: String, text: String = "{}") = myFixture.addFileToProject(path, text)
    private fun file(path: String, text: String = "{}") = psiFile(path, text).virtualFile

    @Test
    fun testConstructor() {
        assertNotNull(Utils())
    }

    @Test
    fun testOpenFile() {
        val file = file("package.json")
        Utils.openFile(project, file)

        assertTrue(FileEditorManager.getInstance(project).isFileOpen(file))
    }

    @Test
    fun testIsInNodeModules() {
        val test = { path: String -> Utils.isInNodeModules(file(path)) }

        assertFalse(test("package.json"))
        assertFalse(test("node/modules/package.json"))
        assertFalse(test("fake_node_modules/package.json"))
        assertTrue(test("node_modules/package.json"))
    }

    @Test
    fun testIsSupportedVirtualFile() {
        val test = { path: String -> Utils.isSupportedFile(file(path)) }

        assertTrue(test("package.json"))
        assertTrue(test("fake_node_modules/package.json"))
        assertFalse(test("yarn.lock"))
        assertFalse(test("package-lock.json"))
        assertFalse(test("fake_package.json"))
        assertFalse(test("node_modules/package.json"))
    }

    @Test
    fun testIsSupportedPsiFile() {
        val test = { path: String -> Utils.isSupportedFile(psiFile(path)) }

        assertTrue(test("package.json"))
        assertTrue(test("fake_node_modules/package.json"))
        assertFalse(test("yarn.lock"))
        assertFalse(test("package-lock.json"))
        assertFalse(test("fake_package.json"))
        assertFalse(test("node_modules/package.json"))
    }

    @Test
    fun testMapFirst() {
        val test = { map: Map<String, Int?> -> map.mapFirst { it.value }}

        assertEquals(test(mapOf("foo" to 1, "bar" to 2)), 1)
        assertEquals(test(mapOf("foo" to null, "bar" to 2)), 2)
        assertEquals(test(emptyMap()), null)
    }

    @Test
    fun testPsiElementFirst() {
        val test = { text: String -> psiFile("foo_${text.hashCode()}.json", text).firstChild }

        assertEquals(test("foo").first(), "foo")
        assertEquals(test("{\"foo\": \"bar\"}").children[0].first(), "foo")
    }

    @Test
    fun testPsiElementLast() {
        val test = { text: String -> psiFile("foo_${text.hashCode()}.json", text).firstChild }

        assertEquals(test("foo").last(), "foo")
        assertEquals(test("{\"foo\": \"bar\"}").children[0].last(), "bar")
    }

    @Test
    fun testMemoizeSingle() {
        val test: (arg: String) -> Int = mock()
        val memoized = test.memoize()

        memoized("foo")
        memoized("foo")
        verify(test, times(1)).invoke("foo")

        memoized("bar")
        memoized("bar")
        memoized("bar")
        verify(test, times(1)).invoke("bar")

        verify(test, times(0)).invoke("buz")

        verify(test, times(2)).invoke(any())
    }

    @Test
    fun testMemoizePair() {
        val test: (arg: String, arg2: String) -> Int = mock()
        val memoized = test.memoize()

        memoized("foo", "foo")
        memoized("foo", "foo")
        verify(test, times(1)).invoke("foo", "foo")

        memoized("bar", "bar")
        memoized("bar", "bar")
        memoized("bar", "bar")
        verify(test, times(1)).invoke("bar", "bar")

        verify(test, times(0)).invoke("buz", "buz")

        verify(test, times(2)).invoke(any(), any())
    }

    @Test
    fun testMemoizeTriple() {
        val test: (arg: String, arg2: String, arg3: String) -> Int = mock()
        val memoized = test.memoize()

        memoized("foo", "foo", "foo")
        memoized("foo", "foo", "foo")
        verify(test, times(1)).invoke("foo", "foo", "foo")

        memoized("bar", "bar", "bar")
        memoized("bar", "bar", "bar")
        memoized("bar", "bar", "bar")
        verify(test, times(1)).invoke("bar", "bar", "bar")

        verify(test, times(0)).invoke("buz", "buz", "buz")

        verify(test, times(2)).invoke(any(), any(), any())
    }
}
