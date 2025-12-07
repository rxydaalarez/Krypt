@file:Suppress("PropertyName")

package xyz.meowing.krypt.updateChecker

import xyz.meowing.krypt.Krypt

internal object StateTracker {
    var dontShowForVersion: String by Krypt.saveData.string("dontShowForVersion")

    var isMessageShown = false
    var latestVersion: String? = null

    var modrinthUrl: String? = null

    var modrinthDownloadUrl: String? = null

    var forceUpdate = false

    data class ModrinthVersion(
        val id: String,
        val version_number: String,
        val date_published: String,
        val game_versions: List<String>,
        val loaders: List<String>,
        val status: String,
        val version_type: String,
        val files: List<ModrinthFile>
    )

    data class ModrinthFile(
        val url: String,
        val filename: String,
        val primary: Boolean
    )
}