package us.mathewtech.client.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import us.mathewtech.registry.ModBlocks
import java.util.concurrent.CompletableFuture

class ModBlockTagProvider(
    output: FabricPackOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagsProvider.BlockTagsProvider(output, registriesFuture) {
    override fun addTags(registries: HolderLookup.Provider) {
        val pairs = buildList {
            ModBlocks.slabsByBase.forEach { (base, carpeted) -> add(base to carpeted) }
            ModBlocks.stairsByBase.forEach { (base, carpeted) -> add(base to carpeted) }
        }

        MIRRORED_TAGS.forEach { tag ->
            val taggedBlocks = pairs
                .filter { (base, _) -> base.defaultBlockState().`is`(tag) }
                .mapNotNull { (_, carpeted) -> BuiltInRegistries.BLOCK.getResourceKey(carpeted).orElse(null) }

            if (taggedBlocks.isNotEmpty()) {
                builder(tag).add(*taggedBlocks.toTypedArray())
            }
        }
    }

    companion object {
        private val MIRRORED_TAGS: List<TagKey<Block>> = listOf(
            BlockTags.SLABS,
            BlockTags.STAIRS,
            BlockTags.WOODEN_SLABS,
            BlockTags.WOODEN_STAIRS,
            BlockTags.STONE_BRICKS,
            BlockTags.BAMBOO_BLOCKS,
            BlockTags.MINEABLE_WITH_AXE,
            BlockTags.MINEABLE_WITH_HOE,
            BlockTags.MINEABLE_WITH_PICKAXE,
            BlockTags.MINEABLE_WITH_SHOVEL,
            BlockTags.NEEDS_STONE_TOOL,
            BlockTags.NEEDS_IRON_TOOL,
            BlockTags.NEEDS_DIAMOND_TOOL,
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            BlockTags.INCORRECT_FOR_GOLD_TOOL,
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            BlockTags.INCORRECT_FOR_COPPER_TOOL,
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            BlockTags.GUARDED_BY_PIGLINS,
            BlockTags.DRAGON_IMMUNE,
            BlockTags.WITHER_IMMUNE,
            BlockTags.FEATURES_CANNOT_REPLACE
        )
    }
}
