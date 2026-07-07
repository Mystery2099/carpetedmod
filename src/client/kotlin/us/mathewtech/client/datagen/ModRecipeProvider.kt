package us.mathewtech.client.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.block.Block
import us.mathewtech.CarpetedMod
import us.mathewtech.registry.ModBlocks
import us.mathewtech.registry.ModItemTags
import us.mathewtech.recipe.CarpetingRecipe
import us.mathewtech.recipe.RecoloringRecipe
import us.mathewtech.recipe.RemovalRecipe
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: FabricPackOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricRecipeProvider(output, registriesFuture) {
    override fun getName(): String {
        return "Recipes"
    }

    override fun createRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput): RecipeProvider {
        return object : RecipeProvider(registries, output) {
            override fun buildRecipes() {
                val itemLookup = registries.lookupOrThrow(Registries.ITEM)
                val carpets = Ingredient.of(itemLookup.getOrThrow(ItemTags.WOOL_CARPETS))
                val dyes = Ingredient.of(itemLookup.getOrThrow(ModItemTags.CARPET_DYES))
                val removers = Ingredient.of(itemLookup.getOrThrow(ModItemTags.CARPET_REMOVERS))

                ModBlocks.slabsByBase.forEach { (base, carpeted) ->
                    addInteractionRecipes(output, base, carpeted, carpets, dyes, removers)
                }

                ModBlocks.stairsByBase.forEach { (base, carpeted) ->
                    addInteractionRecipes(output, base, carpeted, carpets, dyes, removers)
                }
            }
        }
    }

    private fun addInteractionRecipes(
        output: RecipeOutput,
        base: Block,
        carpeted: Block,
        carpets: Ingredient,
        dyes: Ingredient,
        removers: Ingredient
    ) {
        val basePath = registryPath(base)
        val carpetedPath = registryPath(carpeted)
        val baseKey = blockKey(base)
        val carpetedKey = blockKey(carpeted)

        output.accept(recipeKey("carpeting/$basePath"), CarpetingRecipe(baseKey, carpets, carpetedKey), null)
        output.accept(recipeKey("recoloring/$carpetedPath"), RecoloringRecipe(carpetedKey, dyes, carpetedKey), null)
        output.accept(recipeKey("removal/$carpetedPath"), RemovalRecipe(carpetedKey, removers, baseKey), null)
    }

    private fun recipeKey(path: String): ResourceKey<Recipe<*>> {
        return ResourceKey.create(Registries.RECIPE, CarpetedMod.id(path))
    }

    private fun blockKey(block: Block): ResourceKey<Block> {
        return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow()
    }

    private fun registryPath(block: Block): String {
        return BuiltInRegistries.BLOCK.getKey(block).path
    }
}
