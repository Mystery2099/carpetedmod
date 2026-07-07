package us.mathewtech.client.integration.rei

import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.entry.EntryStack
import me.shedaniel.rei.api.common.util.EntryStacks
import me.shedaniel.rei.plugin.common.displays.DefaultOxidationScrapingDisplay
import me.shedaniel.rei.plugin.common.displays.DefaultOxidizingDisplay
import me.shedaniel.rei.plugin.common.displays.DefaultWaxScrapingDisplay
import me.shedaniel.rei.plugin.common.displays.DefaultWaxingDisplay
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.HoneycombItem
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeatheringCopper
import us.mathewtech.block.CarpetSurface
import us.mathewtech.block.CarpetedStairBlock
import us.mathewtech.item.CarpetedItemStacks
import us.mathewtech.registry.ModBlocks
import us.mathewtech.util.CarpetedCopperUtil

object CarpetedReiCopperDisplayFactory {
    fun registerDisplays(registry: DisplayRegistry) {
        registerTransitions(registry, HoneycombItem.WAXABLES.get()) { input, output ->
            DefaultWaxingDisplay(input, output)
        }
        registerTransitions(registry, HoneycombItem.WAX_OFF_BY_BLOCK.get()) { input, output ->
            DefaultWaxScrapingDisplay(input, output)
        }
        registerTransitions(registry, WeatheringCopper.NEXT_BY_BLOCK.get()) { input, output ->
            DefaultOxidizingDisplay(input, output)
        }
        registerTransitions(registry, WeatheringCopper.PREVIOUS_BY_BLOCK.get()) { input, output ->
            DefaultOxidationScrapingDisplay(input, output)
        }
    }

    private fun <T : Display> registerTransitions(
        registry: DisplayRegistry,
        transitions: Map<Block, Block>,
        displayFactory: (EntryStack<*>, EntryStack<*>) -> T
    ) {
        transitions.entries
            .sortedBy { BuiltInRegistries.BLOCK.getKey(it.key) }
            .forEach { (baseBlock, resultBlock) ->
                val carpetedBlock = ModBlocks.carpetedForBase(baseBlock) ?: return@forEach
                val carpetedResult = ModBlocks.carpetedForBase(resultBlock) ?: return@forEach
                if (!CarpetedCopperUtil.isWeatheringCarpeted(carpetedBlock)) {
                    return@forEach
                }

                for (surface in surfacesFor(carpetedBlock)) {
                    for (color in DyeColor.entries) {
                        val input = EntryStacks.of(CarpetedItemStacks.create(carpetedBlock, color, surface))
                        val output = EntryStacks.of(CarpetedItemStacks.create(carpetedResult, color, surface))
                        registry.add(displayFactory(input, output))
                    }
                }
            }
    }

    private fun surfacesFor(block: Block): List<CarpetSurface> {
        return when (block) {
            is CarpetedStairBlock -> CarpetSurface.entries
            else -> listOf(CarpetSurface.TOP)
        }
    }
}
