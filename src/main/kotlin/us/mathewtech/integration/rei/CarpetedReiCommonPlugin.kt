package us.mathewtech.integration.rei

import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry
import me.shedaniel.rei.api.common.plugins.REICommonPlugin
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry
import us.mathewtech.CarpetedMod
import us.mathewtech.recipe.CarpetingRecipe
import us.mathewtech.recipe.RecoloringRecipe
import us.mathewtech.recipe.RemovalRecipe
import us.mathewtech.registry.ModRecipes

class CarpetedReiCommonPlugin : REICommonPlugin {
    override fun registerDisplays(registry: ServerDisplayRegistry) {
        registry.beginRecipeFiller<CarpetingRecipe, CarpetingDisplay>(CarpetingRecipe::class.java)
            .filterType(ModRecipes.CARPETING_TYPE)
            .fillMultiple { holder -> CarpetedReiDisplayFactory.fromCarpeting(holder.value()) }

        registry.beginRecipeFiller<RecoloringRecipe, RecoloringDisplay>(RecoloringRecipe::class.java)
            .filterType(ModRecipes.RECOLORING_TYPE)
            .fillMultiple { holder -> CarpetedReiDisplayFactory.fromRecoloring(holder.value()) }

        registry.beginRecipeFiller<RemovalRecipe, RemovalDisplay>(RemovalRecipe::class.java)
            .filterType(ModRecipes.REMOVAL_TYPE)
            .fillMultiple { holder -> CarpetedReiDisplayFactory.fromRemoval(holder.value()) }
    }

    override fun registerDisplaySerializer(registry: DisplaySerializerRegistry) {
        registry.register(CarpetedMod.id("carpeting"), CarpetingDisplay.SERIALIZER)
        registry.register(CarpetedMod.id("recoloring"), RecoloringDisplay.SERIALIZER)
        registry.register(CarpetedMod.id("removal"), RemovalDisplay.SERIALIZER)
    }
}
