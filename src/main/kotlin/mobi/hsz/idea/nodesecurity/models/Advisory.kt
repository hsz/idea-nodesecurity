package mobi.hsz.idea.nodesecurity.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import com.intellij.openapi.util.text.StringUtil
import com.vdurmont.semver4j.Semver
import com.vdurmont.semver4j.SemverException

data class Advisory(
        val id: Int = 0,
//        val created_at: Date = Date(),
//        val updated_at: Date = Date(),
        val title: String = "",
        val author: String = "",
        val module_name: String = "",
//        val publish_date: Date = Date(),
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
    class Deserializer : ResponseDeserializable<Advisory> {
        override fun deserialize(content: String) = Gson().fromJson(content, Advisory::class.java)!!
        // if Date objects will be required, use Joda-Time with GsonBuilder
//        override fun deserialize(content: String) = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create().fromJson(content, Advisory::class.java)
    }

    fun isVulnerable(version: String): Boolean = try {
        Semver(
                // TODO: get the real version of the installed package
                StringUtil.trimLeading(StringUtil.trimLeading(version, '~'), '^'),
                Semver.SemverType.NPM
        ).satisfies(vulnerable_versions)
    } catch (e: SemverException) {
        false
    }

}
