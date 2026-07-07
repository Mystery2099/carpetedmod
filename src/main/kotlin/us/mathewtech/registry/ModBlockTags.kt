package us.mathewtech.registry

import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import us.mathewtech.CarpetedMod

object ModBlockTags {
    val SUPPORTED_BASE_BLOCKS: TagKey<Block> = blockTag("supported_base_blocks")
    val SUPPORTED_BASE_SLABS: TagKey<Block> = blockTag("supported_base_slabs")
    val SUPPORTED_BASE_STAIRS: TagKey<Block> = blockTag("supported_base_stairs")

    val CARPETED_BLOCKS: TagKey<Block> = blockTag("carpeted_blocks")
    val CARPETED_SLABS: TagKey<Block> = blockTag("carpeted_slabs")
    val CARPETED_STAIRS: TagKey<Block> = blockTag("carpeted_stairs")

    val DISABLE_CARPETING: TagKey<Block> = blockTag("disable_carpeting")
    val DISABLE_RECOLORING: TagKey<Block> = blockTag("disable_recoloring")
    val DISABLE_REMOVAL: TagKey<Block> = blockTag("disable_removal")
    val DISABLE_SILK_TOUCH_PRESERVATION: TagKey<Block> = blockTag("disable_silk_touch_preservation")
    val HIDE_FROM_CARPETED_TAB: TagKey<Block> = blockTag("hide_from_carpeted_tab")

    private fun blockTag(path: String): TagKey<Block> {
        return TagKey.create(Registries.BLOCK, CarpetedMod.id(path))
    }
}
