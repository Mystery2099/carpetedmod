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
