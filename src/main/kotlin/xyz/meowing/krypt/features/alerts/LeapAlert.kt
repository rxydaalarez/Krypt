package xyz.meowing.krypt.features.alerts

import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped
import xyz.meowing.knit.api.KnitChat
import xyz.meowing.krypt.annotations.Module
import xyz.meowing.krypt.api.location.SkyBlockIsland
import xyz.meowing.krypt.config.ConfigDelegate
import xyz.meowing.krypt.config.ui.elements.base.ElementType
import xyz.meowing.krypt.events.core.ChatEvent
import xyz.meowing.krypt.features.Feature
import xyz.meowing.krypt.managers.config.ConfigElement
import xyz.meowing.krypt.managers.config.ConfigManager

@Module
object LeapAlert : Feature(
    "leapAlert",
    island = SkyBlockIsland.THE_CATACOMBS
) {
    private val leapRegex = "^You have teleported to (.+)".toRegex()
    private val leapMessage by ConfigDelegate<String>("leapAnnounce.message")

    override fun addConfig() {
        ConfigManager
            .addFeature(
                "Leap alert",
                "Announces in party chat when you use a leap",
                "Alerts",
                ConfigElement(
                    "leapAnnounce",
                    ElementType.Switch(false)
                )
            )
            .addFeatureOption(
                "Message",
                ConfigElement(
                    "leapAnnounce.message",
                    ElementType.TextInput("Leaping to", "Leaping to")
                )
            )
    }

    override fun initialize() {
        register<ChatEvent.Receive> { event ->
            val result = leapRegex.find(event.message.stripped)
            if (result != null) KnitChat.sendCommand("pc $leapMessage ${result.groupValues[1]}")
        }
    }
}