package us.mathewtech.client.integration.cloth

import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import us.mathewtech.config.CarpetedModConfig

object CarpetedClothConfigScreen {
    fun create(parent: Screen?): Screen {
        val config = CarpetedModConfig.snapshot()
        val builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.translatable("config.carpeted-mod.title"))
            .setSavingRunnable { CarpetedModConfig.replace(config) }

        val entries = builder.entryBuilder()

        val gameplay = builder.getOrCreateCategory(Component.translatable("config.carpeted-mod.category.gameplay"))
        gameplay.addEntry(
            entries.startBooleanToggle(
                Component.translatable("config.carpeted-mod.option.enable_carpet_application"),
                config.enableCarpetApplication
            )
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.carpeted-mod.option.enable_carpet_application.tooltip"))
                .setSaveConsumer { config.enableCarpetApplication = it }
                .build()
        )
        gameplay.addEntry(
            entries.startBooleanToggle(
                Component.translatable("config.carpeted-mod.option.enable_carpet_recoloring"),
                config.enableCarpetRecoloring
            )
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.carpeted-mod.option.enable_carpet_recoloring.tooltip"))
                .setSaveConsumer { config.enableCarpetRecoloring = it }
                .build()
        )
        gameplay.addEntry(
            entries.startBooleanToggle(
                Component.translatable("config.carpeted-mod.option.enable_carpet_removal"),
                config.enableCarpetRemoval
            )
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.carpeted-mod.option.enable_carpet_removal.tooltip"))
                .setSaveConsumer { config.enableCarpetRemoval = it }
                .build()
        )

        val drops = builder.getOrCreateCategory(Component.translatable("config.carpeted-mod.category.drops"))
        drops.addEntry(
            entries.startBooleanToggle(
                Component.translatable("config.carpeted-mod.option.preserve_silk_touch_drops"),
                config.preserveCarpetedDropsWithSilkTouch
            )
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.carpeted-mod.option.preserve_silk_touch_drops.tooltip"))
                .setSaveConsumer { config.preserveCarpetedDropsWithSilkTouch = it }
                .build()
        )

        return builder.build()
    }
}
