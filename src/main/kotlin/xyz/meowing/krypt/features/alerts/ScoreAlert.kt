package xyz.meowing.krypt.features.alerts

import xyz.meowing.knit.api.KnitChat
import xyz.meowing.krypt.annotations.Module
import xyz.meowing.krypt.config.ConfigDelegate
import xyz.meowing.krypt.config.ui.elements.base.ElementType
import xyz.meowing.krypt.managers.config.ConfigElement
import xyz.meowing.krypt.managers.config.ConfigManager
import xyz.meowing.krypt.utils.TitleUtils

@Module
object ScoreAlert {
    private val enabled by ConfigDelegate<Boolean>("scoreAlert")
    private val showTitle by ConfigDelegate<Boolean>("scoreAlert.showTitle")
    private val twoSeventyMessage by ConfigDelegate<String>("scoreAlert.270message")
    private val threeHundredMessage by ConfigDelegate<String>("scoreAlert.300message")

    init {
        ConfigManager
            .addFeature(
                "Score alert",
                "Announces in party chat when you hit score milestones",
                "Alerts",
                ConfigElement(
                    "scoreAlert",
                    ElementType.Switch(false)
                )
            )
            .addFeatureOption(
                "Show title",
                ConfigElement(
                    "scoreAlert.showTitle",
                    ElementType.Switch(true)
                )
            )
            .addFeatureOption(
                "270 score message",
                ConfigElement(
                    "scoreAlert.270message",
                    ElementType.TextInput("[Krypt] 270 score!", "[Krypt] 270 score!")
                )
            )
            .addFeatureOption(
                "300 score message",
                ConfigElement(
                    "scoreAlert.300message",
                    ElementType.TextInput("[Krypt] 300 score!", "[Krypt] 300 score!")
                )
            )
    }

    fun show270() {
        if (!enabled) return

        if (showTitle) TitleUtils.showTitle("§b270!", duration = 2000)
        if (twoSeventyMessage.isNotEmpty()) KnitChat.sendCommand("pc $twoSeventyMessage")
    }

    fun show300() {
        if (!enabled) return

        if (showTitle) TitleUtils.showTitle("§a300!", duration = 2000)
        if (threeHundredMessage.isNotEmpty()) KnitChat.sendCommand("pc $threeHundredMessage")
    }
}