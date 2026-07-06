package us.mathewtech.client.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import us.mathewtech.registry.ModBlocks
import java.util.Locale
import java.util.concurrent.CompletableFuture

class ModLangProvider(
    output: FabricPackOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricLanguageProvider(output, registriesFuture) {
    override fun generateTranslations(
        registryLookup: HolderLookup.Provider,
        translationBuilder: TranslationBuilder
    ) {
        translationBuilder.add("itemGroup.carpeted-mod.carpeted_blocks", "Carpeted Blocks")
        translationBuilder.add("item.carpeted-mod.carpeted_block", "%s Carpeted %s")
        translationBuilder.add("modmenu.carpeted-mod.title", "Carpeted Mod")
        translationBuilder.add("modmenu.carpeted-mod.line.apply", "Use vanilla carpet on supported slabs and stairs to apply carpet.")
        translationBuilder.add("modmenu.carpeted-mod.line.dye", "Use dye on carpeted blocks to recolor the carpet.")
        translationBuilder.add("modmenu.carpeted-mod.line.remove", "Use shears, or another item in carpeted-mod:carpet_removers, to remove carpet.")
        translationBuilder.add("modmenu.carpeted-mod.line.silk_touch", "Silk Touch preserves carpeted block items and their carpet color.")
        translationBuilder.add("modmenu.carpeted-mod.line.tags", "Pack makers can adjust carpeted-mod:carpet_dyes and carpeted-mod:carpet_removers.")
        translationBuilder.add("modmenu.carpeted-mod.line.scope", "Prototype 1 supports vanilla carpets, registered vanilla slabs, and registered vanilla stairs.")
        translationBuilder.add("config.carpeted-mod.title", "Carpeted Mod Settings")
        translationBuilder.add("config.carpeted-mod.category.gameplay", "Gameplay")
        translationBuilder.add("config.carpeted-mod.category.drops", "Drops")
        translationBuilder.add("config.carpeted-mod.option.enable_carpet_application", "Enable carpet application")
        translationBuilder.add("config.carpeted-mod.option.enable_carpet_application.tooltip", "Allows players to apply carpets to supported slabs and stairs.")
        translationBuilder.add("config.carpeted-mod.option.enable_carpet_recoloring", "Enable carpet recoloring")
        translationBuilder.add("config.carpeted-mod.option.enable_carpet_recoloring.tooltip", "Allows dye items to recolor existing carpeted blocks.")
        translationBuilder.add("config.carpeted-mod.option.enable_carpet_removal", "Enable carpet removal")
        translationBuilder.add("config.carpeted-mod.option.enable_carpet_removal.tooltip", "Allows shears and carpeted-mod:carpet_removers items to remove carpet.")
        translationBuilder.add("config.carpeted-mod.option.preserve_silk_touch_drops", "Preserve carpeted drops with Silk Touch")
        translationBuilder.add("config.carpeted-mod.option.preserve_silk_touch_drops.tooltip", "Makes Silk Touch drop the carpeted block item instead of separate base block and carpet items.")
        translationBuilder.add("category.carpeted-mod.carpeting", "Carpeting")
        translationBuilder.add("category.carpeted-mod.recoloring", "Recoloring")
        translationBuilder.add("category.carpeted-mod.removal", "Carpet Removal")
        translationBuilder.add("advancements.carpeted-mod.root.title", "Soft Steps")
        translationBuilder.add("advancements.carpeted-mod.root.description", "Apply carpet to a slab or stair.")
        translationBuilder.add("advancements.carpeted-mod.dye_carpet.title", "A Fresh Shade")
        translationBuilder.add("advancements.carpeted-mod.dye_carpet.description", "Use dye to recolor a carpeted slab or stair.")
        translationBuilder.add("advancements.carpeted-mod.remove_carpet.title", "Clean Removal")
        translationBuilder.add("advancements.carpeted-mod.remove_carpet.description", "Use shears to remove carpet from a slab or stair.")
        translationBuilder.add("advancements.carpeted-mod.silk_touch_carpeted_block.title", "Move-In Ready")
        translationBuilder.add("advancements.carpeted-mod.silk_touch_carpeted_block.description", "Break a carpeted slab or stair with Silk Touch.")

        ModBlocks.slabsByBase.values.forEach { block ->
            translationBuilder.add(block, "Carpeted ${displayName(block)}")
        }

        ModBlocks.stairsByBase.values.forEach { block ->
            translationBuilder.add(block, "Carpeted ${displayName(block)}")
        }
    }

    private fun displayName(block: net.minecraft.world.level.block.Block): String {
        return BuiltInRegistries.BLOCK.getKey(block)
            .path
            .split("_")
            .joinToString(" ") { word ->
                word.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString()
                }
            }
    }
}
