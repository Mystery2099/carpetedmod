package us.mathewtech.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.block.state.BlockState

data class CarpetInteractionRecipeInput(
    val state: BlockState,
    val interactionItem: ItemStack
) : RecipeInput {
    private val blockStack: ItemStack = ItemStack(state.block)

    override fun getItem(index: Int): ItemStack {
        return when (index) {
            0 -> blockStack
            1 -> interactionItem
            else -> throw IllegalArgumentException("No item for index $index")
        }
    }

    override fun size(): Int {
        return 2
    }
}
