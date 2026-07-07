package us.mathewtech.registry

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import us.mathewtech.CarpetedMod
import us.mathewtech.recipe.CarpetingRecipe
import us.mathewtech.recipe.RecoloringRecipe
import us.mathewtech.recipe.RemovalRecipe

object ModRecipes {
    private var initialized = false

    val CARPETING_TYPE: RecipeType<CarpetingRecipe> = registerType("carpeting")
    val RECOLORING_TYPE: RecipeType<RecoloringRecipe> = registerType("recoloring")
    val REMOVAL_TYPE: RecipeType<RemovalRecipe> = registerType("removal")

    fun initialize() {
        if (initialized) {
            return
        }

        initialized = true
        registerSerializer("carpeting", CarpetingRecipe.SERIALIZER)
        registerSerializer("recoloring", RecoloringRecipe.SERIALIZER)
        registerSerializer("removal", RemovalRecipe.SERIALIZER)
    }

    private fun <T : Recipe<*>> registerSerializer(path: String, serializer: RecipeSerializer<T>) {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, CarpetedMod.id(path), serializer)
    }

    private fun <T : Recipe<*>> registerType(path: String): RecipeType<T> {
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, CarpetedMod.id(path), object : RecipeType<T> {
            override fun toString(): String {
                return Identifier.fromNamespaceAndPath(CarpetedMod.MOD_ID, path).toString()
            }
        })
    }
}
