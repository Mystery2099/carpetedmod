package us.mathewtech.client.integration.rei

import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.common.util.EntryIngredients
import net.minecraft.tags.ItemTags
import us.mathewtech.integration.rei.CarpetedReiCategoryIds
import us.mathewtech.registry.ModItemTags

class CarpetedReiClientPlugin : REIClientPlugin {
    override fun registerCategories(registry: CategoryRegistry) {
        registry.add(CarpetingCategory())
        registry.add(RecoloringCategory())
        registry.add(RemovalCategory())

        registry.addWorkstations(CarpetedReiCategoryIds.CARPETING, EntryIngredients.ofItemTag(ItemTags.WOOL_CARPETS))
        registry.addWorkstations(CarpetedReiCategoryIds.RECOLORING, EntryIngredients.ofItemTag(ModItemTags.CARPET_DYES))
        registry.addWorkstations(CarpetedReiCategoryIds.REMOVAL, EntryIngredients.ofItemTag(ModItemTags.CARPET_REMOVERS))
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        CarpetedReiCopperDisplayFactory.registerDisplays(registry)
    }
}
