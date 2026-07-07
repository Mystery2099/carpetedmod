package us.mathewtech.recipe

import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.Block
import us.mathewtech.registry.ModRecipes

class RecoloringRecipe(
    inputBlockKey: ResourceKey<Block>,
    ingredient: Ingredient,
    resultBlockKey: ResourceKey<Block>
) : AbstractCarpetInteractionRecipe(inputBlockKey, ingredient, resultBlockKey) {
    override fun getSerializer(): RecipeSerializer<out RecoloringRecipe> {
        return SERIALIZER
    }

    override fun getType(): RecipeType<out RecoloringRecipe> {
        return ModRecipes.RECOLORING_TYPE
    }

    companion object {
        val SERIALIZER: RecipeSerializer<RecoloringRecipe> = serializer(::RecoloringRecipe)
    }
}
