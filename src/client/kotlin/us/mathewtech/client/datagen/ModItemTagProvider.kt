package us.mathewtech.client.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Items
import us.mathewtech.registry.ModItemTags
import java.util.concurrent.CompletableFuture

class ModItemTagProvider(
    output: FabricPackOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagsProvider.ItemTagsProvider(output, registriesFuture) {
    override fun addTags(registries: HolderLookup.Provider) {
        builder(ModItemTags.CARPET_REMOVERS).add(BuiltInRegistries.ITEM.getResourceKey(Items.SHEARS).orElseThrow())
    }
}
