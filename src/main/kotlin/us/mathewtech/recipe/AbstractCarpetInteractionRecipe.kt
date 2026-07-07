package us.mathewtech.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.PlacementInfo
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeBookCategories
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

abstract class AbstractCarpetInteractionRecipe(
    val inputBlockKey: ResourceKey<Block>,
    val ingredient: Ingredient,
    val resultBlockKey: ResourceKey<Block>
) : Recipe<CarpetInteractionRecipeInput> {
    override fun matches(input: CarpetInteractionRecipeInput, level: Level): Boolean {
        return BuiltInRegistries.BLOCK.getResourceKey(input.state.block).orElse(null) == inputBlockKey &&
            ingredient.test(input.interactionItem)
    }

    override fun assemble(input: CarpetInteractionRecipeInput): ItemStack {
        return ItemStack(resultBlock().asItem())
    }

    override fun isSpecial(): Boolean {
        return true
    }

    override fun showNotification(): Boolean {
        return false
    }

    override fun group(): String {
        return ""
    }

    override fun placementInfo(): PlacementInfo {
        val inputItem = inputBlock().asItem()
        return PlacementInfo.create(listOf(Ingredient.of(inputItem), ingredient))
    }

    override fun recipeBookCategory(): RecipeBookCategory {
        return RecipeBookCategories.CRAFTING_MISC
    }

    fun inputBlock(): Block {
        return BuiltInRegistries.BLOCK.getValue(inputBlockKey.identifier())
    }

    fun resultBlock(): Block {
        return BuiltInRegistries.BLOCK.getValue(resultBlockKey.identifier())
    }

    companion object {
        fun <T : AbstractCarpetInteractionRecipe> serializer(
            constructor: (ResourceKey<Block>, Ingredient, ResourceKey<Block>) -> T
        ): RecipeSerializer<T> {
            return RecipeSerializer(mapCodec(constructor), streamCodec(constructor))
        }

        private fun <T : AbstractCarpetInteractionRecipe> mapCodec(
            constructor: (ResourceKey<Block>, Ingredient, ResourceKey<Block>) -> T
        ): MapCodec<T> {
            return RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    ResourceKey.codec(Registries.BLOCK).fieldOf("input_block").forGetter(AbstractCarpetInteractionRecipe::inputBlockKey),
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(AbstractCarpetInteractionRecipe::ingredient),
                    ResourceKey.codec(Registries.BLOCK).fieldOf("result_block").forGetter(AbstractCarpetInteractionRecipe::resultBlockKey)
                ).apply(instance, constructor)
            }
        }

        private fun <T : AbstractCarpetInteractionRecipe> streamCodec(
            constructor: (ResourceKey<Block>, Ingredient, ResourceKey<Block>) -> T
        ): StreamCodec<RegistryFriendlyByteBuf, T> {
            return StreamCodec.composite(
                ResourceKey.streamCodec(Registries.BLOCK),
                AbstractCarpetInteractionRecipe::inputBlockKey,
                Ingredient.CONTENTS_STREAM_CODEC,
                AbstractCarpetInteractionRecipe::ingredient,
                ResourceKey.streamCodec(Registries.BLOCK),
                AbstractCarpetInteractionRecipe::resultBlockKey,
                constructor
            )
        }
    }
}
