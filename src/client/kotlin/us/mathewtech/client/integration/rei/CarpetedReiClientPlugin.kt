package us.mathewtech.client.integration.rei

import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import us.mathewtech.block.CarpetSurface
import us.mathewtech.block.CarpetedBlock
import us.mathewtech.block.CarpetedStairBlock
import us.mathewtech.item.CarpetedItemStacks
import us.mathewtech.registry.ModBlocks
import us.mathewtech.util.CarpetColorUtil

class CarpetedReiClientPlugin : REIClientPlugin {
    override fun registerCategories(registry: CategoryRegistry) {
        registry.add(CarpetingCategory())
        registry.add(RecoloringCategory())
        registry.add(RemovalCategory())

        registry.addWorkstations(CarpetedReiCategoryIds.CARPETING, EntryStacks.of(CarpetColorUtil.carpetItem(DyeColor.WHITE)))
        registry.addWorkstations(CarpetedReiCategoryIds.RECOLORING, EntryStacks.of(CarpetColorUtil.dyeItem(DyeColor.MAGENTA)))
        registry.addWorkstations(CarpetedReiCategoryIds.REMOVAL, EntryStacks.of(Items.SHEARS))
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        carpetedBlocks().forEach { block ->
            registry.add(createCarpetingDisplay(block))
            registry.add(createRecoloringDisplay(block))
            registry.add(createRemovalDisplay(block))
        }
    }

    private fun createCarpetingDisplay(block: Block): CarpetingDisplay {
        return CarpetingDisplay(
            unifiedIngredient(listOf(ItemStack(block.asItem()))),
            unifiedIngredient(listOf(carpetedStack(block, DyeColor.WHITE)))
        )
    }

    private fun createRecoloringDisplay(block: Block): RecoloringDisplay {
        return RecoloringDisplay(
            unifiedIngredient(listOf(carpetedStack(block, DyeColor.WHITE))),
            unifiedIngredient(listOf(carpetedStack(block, DyeColor.MAGENTA)))
        )
    }

    private fun createRemovalDisplay(block: Block): RemovalDisplay {
        return RemovalDisplay(
            unifiedIngredient(listOf(carpetedStack(block, DyeColor.WHITE))),
            unifiedIngredient(listOf(ItemStack(block.asItem()))),
            unifiedIngredient(listOf(ItemStack(CarpetColorUtil.carpetItem(DyeColor.WHITE))))
        )
    }

    private fun carpetedStack(block: Block, color: DyeColor): ItemStack {
        val surface = when (block) {
            is CarpetedStairBlock -> CarpetSurface.TREAD
            else -> CarpetSurface.TOP
        }

        return CarpetedItemStacks.create(block, color, surface)
    }

    private fun carpetedBlocks(): List<Block> {
        return buildList {
            addAll(ModBlocks.slabsByBase.values)
            addAll(ModBlocks.stairsByBase.values)
        }.filterIsInstance<CarpetedBlock>()
            .map { it as Block }
    }
}
