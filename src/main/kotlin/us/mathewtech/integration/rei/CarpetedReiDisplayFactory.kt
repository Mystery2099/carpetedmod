package us.mathewtech.integration.rei

import me.shedaniel.rei.api.common.util.EntryIngredients
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import us.mathewtech.block.CarpetSurface
import us.mathewtech.block.CarpetedStairBlock
import us.mathewtech.item.CarpetedItemStacks
import us.mathewtech.recipe.CarpetingRecipe
import us.mathewtech.recipe.RecoloringRecipe
import us.mathewtech.recipe.RemovalRecipe
import us.mathewtech.registry.ModItemTags

object CarpetedReiDisplayFactory {
    fun fromCarpeting(recipe: CarpetingRecipe): List<CarpetingDisplay> {
        val input = EntryIngredients.of(ItemStack(recipe.inputBlock().asItem()))
        val resultBlock = recipe.resultBlock()

        return DyeColor.entries.map { color ->
            CarpetingDisplay(
                listOf(input, carpetIngredient(color)),
                listOf(EntryIngredients.of(carpetedStack(resultBlock, color)))
            )
        }
    }

    fun fromRecoloring(recipe: RecoloringRecipe): List<RecoloringDisplay> {
        val input = carpetedBlockIngredient(recipe.inputBlock())
        val resultBlock = recipe.resultBlock()

        return DyeColor.entries.map { color ->
            RecoloringDisplay(
                listOf(input, dyeIngredient(color)),
                listOf(EntryIngredients.of(carpetedStack(resultBlock, color)))
            )
        }
    }

    fun fromRemoval(recipe: RemovalRecipe): List<RemovalDisplay> {
        val remover = EntryIngredients.ofIngredient(recipe.ingredient)
        val result = EntryIngredients.of(ItemStack(recipe.resultBlock().asItem()))
        val inputBlock = recipe.inputBlock()

        return DyeColor.entries.map { color ->
            RemovalDisplay(
                listOf(EntryIngredients.of(carpetedStack(inputBlock, color)), remover),
                listOf(result, carpetIngredient(color))
            )
        }
    }

    private fun carpetIngredient(color: DyeColor) =
        EntryIngredients.ofItemTag(ModItemTags.CARPETS_BY_COLOR.getValue(color))

    private fun dyeIngredient(color: DyeColor) =
        EntryIngredients.ofItemTag(ModItemTags.CARPET_DYES_BY_COLOR.getValue(color))

    private fun carpetedBlockIngredient(block: Block) =
        EntryIngredients.from(DyeColor.entries) { color ->
            EntryStacks.of(carpetedStack(block, color))
        }

    private fun carpetedStack(block: Block, color: DyeColor): ItemStack {
        val surface = when (block) {
            is CarpetedStairBlock -> CarpetSurface.TREAD
            else -> CarpetSurface.TOP
        }

        return CarpetedItemStacks.create(block, color, surface)
    }
}
