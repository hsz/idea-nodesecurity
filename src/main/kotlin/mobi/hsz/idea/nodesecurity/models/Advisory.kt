package mobi.hsz.idea.nodesecurity.models

import com.vdurmont.semver4j.Semver
import com.vdurmont.semver4j.SemverException

data class Advisory(
        val id: Int = 0,
        val title: String = "",
        val author: String = "",
        val module_name: String = "",
        val cves: List<String> = emptyList(),
        val vulnerable_versions: String = "",
        val patched_versions: String = "",
        val slug: String = "",
        val overview: String = "",
        val recommendation: String = "",
        val references: String = "",
        val legacy_slug: String = "",
        val allowed_scopes: List<String> = emptyList(),
        val cvss_vector: String = "",
        val cvss_score: Float = 0F,
        val cwe: String = ""
) {
    fun isVulnerable(version: String?): Boolean = try {
        val semver = Semver(version, Semver.SemverType.NPM)
        version !== null && !version.contains('x') && vulnerable_versions.split("||").any { semver.satisfies(it) }
    } catch (e: SemverException) {
        false
    }
}
