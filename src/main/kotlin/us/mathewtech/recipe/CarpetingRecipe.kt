package us.mathewtech.recipe

import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.Block
import us.mathewtech.registry.ModRecipes

class CarpetingRecipe(
    inputBlockKey: ResourceKey<Block>,
    ingredient: Ingredient,
    resultBlockKey: ResourceKey<Block>
) : AbstractCarpetInteractionRecipe(inputBlockKey, ingredient, resultBlockKey) {
    override fun getSerializer(): RecipeSerializer<out CarpetingRecipe> {
        return SERIALIZER
    }

    override fun getType(): RecipeType<out CarpetingRecipe> {
        return ModRecipes.CARPETING_TYPE
    }

    companion object {
        val SERIALIZER: RecipeSerializer<CarpetingRecipe> = serializer(::CarpetingRecipe)
    }
}
