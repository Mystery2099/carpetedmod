package us.mathewtech.client.integration.rei

import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.display.DisplaySerializer
import me.shedaniel.rei.api.common.display.basic.BasicDisplay
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.util.EntryIngredients
import net.minecraft.world.item.ItemStack

class CarpetingDisplay(
    val baseBlock: EntryIngredient,
    val result: EntryIngredient
) : BasicDisplay(listOf(baseBlock), listOf(result)) {
    override fun getCategoryIdentifier() = CarpetedReiCategoryIds.CARPETING

    override fun getSerializer(): DisplaySerializer<out Display>? = null
}

class RecoloringDisplay(
    val source: EntryIngredient,
    val result: EntryIngredient
) : BasicDisplay(listOf(source), listOf(result)) {
    override fun getCategoryIdentifier() = CarpetedReiCategoryIds.RECOLORING

    override fun getSerializer(): DisplaySerializer<out Display>? = null
}

class RemovalDisplay(
    val source: EntryIngredient,
    val baseBlock: EntryIngredient,
    val carpet: EntryIngredient
) : BasicDisplay(listOf(source), listOf(baseBlock, carpet)) {
    override fun getCategoryIdentifier() = CarpetedReiCategoryIds.REMOVAL

    override fun getSerializer(): DisplaySerializer<out Display>? = null
}

fun unifiedIngredient(stacks: List<ItemStack>): EntryIngredient {
    return EntryIngredients.ofItemStacks(stacks)
}
