package xyz.meowing.krypt.updateChecker

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import xyz.meowing.knit.api.KnitClient
import xyz.meowing.knit.api.KnitClient.client
import xyz.meowing.knit.api.scheduler.TickScheduler
import xyz.meowing.krypt.Krypt
import xyz.meowing.krypt.Krypt.MODRINTH_PROJECT_ID
import xyz.meowing.krypt.utils.NetworkUtils.createConnection
import java.util.concurrent.CompletableFuture

object UpdateChecker {
    fun check() {
        CompletableFuture.supplyAsync {
            val modrinth = checkModrinth()
            val latest = listOfNotNull( modrinth?.first)
                .maxByOrNull { compareVersions(it, Krypt.modInfo.version) }
                ?: return@supplyAsync

            if (
                (compareVersions(latest, Krypt.modInfo.version) > 0 && latest != StateTracker.dontShowForVersion) ||
                StateTracker.forceUpdate
                ) {
                StateTracker.isMessageShown = true
                StateTracker.forceUpdate = false
                StateTracker.latestVersion = latest
                StateTracker.modrinthUrl = modrinth?.second
                StateTracker.modrinthDownloadUrl = modrinth?.third
                TickScheduler.Client.post {
                    client.setScreen(UpdateGUI())
                }
            }
        }
    }

    private fun checkModrinth(): Triple<String, String, String?>? = runCatching {
        val connection = createConnection("https://api.modrinth.com/v2/project/${MODRINTH_PROJECT_ID}/version")
        connection.requestMethod = "GET"

        if (connection.responseCode == 200) {
            val versions: List<StateTracker.ModrinthVersion> = Gson().fromJson(
                connection.inputStream.reader(),
                object : TypeToken<List<StateTracker.ModrinthVersion>>() {}.type
            )

            val filteredVersions =
                versions.filter {
                    it.loaders.contains("fabric") && it.status == "listed" && it.version_type == "release" && it.game_versions.contains(KnitClient.minecraftVersion)
                }

            filteredVersions.maxByOrNull { it.date_published }?.let { version ->
                val primaryFile = version.files.firstOrNull { it.primary } ?: version.files.firstOrNull()
                primaryFile?.let {
                    Triple(version.version_number, "https://modrinth.com/mod/${MODRINTH_PROJECT_ID}/version/${version.id}", it.url)
                }
            }
        } else null
    }.getOrNull()

    private fun compareVersions(v1: String, v2: String): Int {
        val parts1 = v1.replace(Regex("[^0-9.]"), "").split(".").map { it.toIntOrNull() ?: 0 }
        val parts2 = v2.replace(Regex("[^0-9.]"), "").split(".").map { it.toIntOrNull() ?: 0 }
        for (i in 0 until maxOf(parts1.size, parts2.size)) {
            val p1 = parts1.getOrElse(i) { 0 }
            val p2 = parts2.getOrElse(i) { 0 }
            if (p1 != p2) return p1 - p2
        }
        return 0
    }
}